/*
 * Copyright 2009 IIZUKA Software Technologies Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.jtype;

import java.io.Serializable;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * Provides utility methods for working with types.
 * 
 * @author Mark Hobson
 * @version $Id$
 */
public final class TypeUtils
{
	// constants --------------------------------------------------------------
	
	private static final Map<Class<?>, Set<Class<?>>> SUBTYPES_BY_PRIMITIVE;
	
	static
	{
		Map<Class<?>, Set<Class<?>>> subtypesByPrimitive = new HashMap<Class<?>, Set<Class<?>>>();
		
		putPrimitiveSubtypes(subtypesByPrimitive, Void.TYPE);
		putPrimitiveSubtypes(subtypesByPrimitive, Boolean.TYPE);
		putPrimitiveSubtypes(subtypesByPrimitive, Byte.TYPE);
		putPrimitiveSubtypes(subtypesByPrimitive, Character.TYPE);
		putPrimitiveSubtypes(subtypesByPrimitive, Short.TYPE, Byte.TYPE);
		putPrimitiveSubtypes(subtypesByPrimitive, Integer.TYPE, Character.TYPE, Short.TYPE);
		putPrimitiveSubtypes(subtypesByPrimitive, Long.TYPE, Integer.TYPE);
		putPrimitiveSubtypes(subtypesByPrimitive, Float.TYPE, Long.TYPE);
		putPrimitiveSubtypes(subtypesByPrimitive, Double.TYPE, Float.TYPE);

		SUBTYPES_BY_PRIMITIVE = Collections.unmodifiableMap(subtypesByPrimitive);
	}
	
	// constructors -----------------------------------------------------------
	
	private TypeUtils()
	{
		throw new AssertionError();
	}
	
	// public methods ---------------------------------------------------------
	
	public static boolean isAssignable(Type supertype, Type type)
	{
		Utils.checkNotNull(supertype, "supertype");
		Utils.checkNotNull(type, "type");
		
		if (supertype.equals(type))
		{
			return true;
		}
		
		if (supertype instanceof Class<?>)
		{
			if (type instanceof Class<?>)
			{
				return isClassAssignable((Class<?>) supertype, (Class<?>) type);
			}
			
			if (type instanceof ParameterizedType)
			{
				return isAssignable(supertype, ((ParameterizedType) type).getRawType());
			}
			
			if (type instanceof TypeVariable<?>)
			{
				return isTypeVariableAssignable(supertype, (TypeVariable<?>) type);
			}
			
			if (type instanceof GenericArrayType)
			{
				if (((Class<?>) supertype).isArray())
				{
					return isAssignable(getComponentType(supertype), getComponentType(type));
				}
				
				return isArraySupertype((Class<?>) supertype);
			}
			
			return false;
		}
		
		if (supertype instanceof ParameterizedType)
		{
			if (type instanceof Class<?>)
			{
				return isSuperAssignable(supertype, type);
			}
			
			if (type instanceof ParameterizedType)
			{
				return isParameterizedTypeAssignable((ParameterizedType) supertype, (ParameterizedType) type);
			}
			
			return false;
		}
		
		if (type instanceof TypeVariable<?>)
		{
			return isTypeVariableAssignable(supertype, (TypeVariable<?>) type);
		}
		
		if (supertype instanceof GenericArrayType)
		{
			if (isArray(type))
			{
				return isAssignable(getComponentType(supertype), getComponentType(type));
			}
			
			return false;
		}
		
		if (supertype instanceof WildcardType)
		{
			return isWildcardTypeAssignable((WildcardType) supertype, type);
		}
		
		return false;
	}
	
	public static boolean isInstance(Type type, Object object)
	{
		return getErasedReferenceType(type).isInstance(object);
	}
	
	/**
	 * Gets the erased type of the specified type.
	 * 
	 * @param type
	 *            the type to perform erasure on
	 * @return the erased type, never a parameterized type nor a type variable
	 * @see <a href="http://java.sun.com/docs/books/jls/third_edition/html/typesValues.html#4.6">4.6 Type Erasure</a>
	 */
	public static Type getErasedType(Type type)
	{
		// the erasure of a parameterized type G<T1, ... ,Tn> is |G|
		if (type instanceof ParameterizedType)
		{
			Type rawType = ((ParameterizedType) type).getRawType();
			
			return getErasedType(rawType);
		}
		
		// TODO: the erasure of a nested type T.C is |T|.C
		
		// the erasure of an array type T[] is |T|[]
		if (isArray(type))
		{
			Type componentType = getComponentType(type);
			Type erasedComponentType = getErasedType(componentType);
			
			return getArrayType(erasedComponentType);
		}
		
		// the erasure of a type variable is the erasure of its leftmost bound 
		if (type instanceof TypeVariable<?>)
		{
			Type[] bounds = ((TypeVariable<?>) type).getBounds();
			
			return getErasedType(bounds[0]);
		}
		
		// the erasure of every other type is the type itself
		return type;
	}
	
	public static Class<?> getErasedReferenceType(Type type)
	{
		Utils.checkTrue(isReferenceType(type), "type is not a reference type: ", type);
		
		return (Class<?>) getErasedType(type);
	}

	/**
	 * @deprecated Use {@link #getErasedReferenceType(Type)} instead.
	 */
	@Deprecated
	public static Class<?> getRawType(Type type)
	{
		return getErasedReferenceType(type);
	}
	
	public static boolean isArray(Type type)
	{
		return (type instanceof Class<?> && ((Class<?>) type).isArray())
			|| (type instanceof GenericArrayType);
	}
	
	public static Type getComponentType(Type type)
	{
		if (type instanceof Class<?>)
		{
			Class<?> klass = (Class<?>) type;
			
			return klass.isArray() ? klass.getComponentType() : null;
		}
		
		if (type instanceof GenericArrayType)
		{
			return ((GenericArrayType) type).getGenericComponentType();
		}
		
		return null;
	}
	
	public static Type getArrayType(Type componentType)
	{
		Utils.checkNotNull(componentType, "componentType");
		
		if (componentType instanceof Class<?>)
		{
			return ClassUtils.getArrayType((Class<?>) componentType);
		}
		
		return Types.genericArrayType(componentType);
	}
	
	public static boolean isSimpleParameterizedType(Type type, Class<?> rawType)
	{
		Utils.checkNotNull(type, "type");
		Utils.checkNotNull(rawType, "rawType");
		
		if (!(type instanceof ParameterizedType))
		{
			return false;
		}
		
		ParameterizedType paramType = (ParameterizedType) type;
		
		Type paramRawType = paramType.getRawType();
		
		if (!(paramRawType instanceof Class<?>))
		{
			return false;
		}
		
		Class<?> paramRawClass = (Class<?>) paramRawType;
		
		if (!rawType.isAssignableFrom(paramRawClass))
		{
			return false;
		}
		
		Type[] typeArgs = paramType.getActualTypeArguments();
		
		return (typeArgs.length == 1);
	}
	
	public static Type getActualTypeArgument(Type type)
	{
		Utils.checkNotNull(type, "type");
		
		ParameterizedType paramType = (ParameterizedType) type;
		
		Type[] typeArgs = paramType.getActualTypeArguments();
		
		Utils.checkTrue(typeArgs.length == 1, "type must be a ParameterizedType with one actual type argument: ", type);
		
		return typeArgs[0];
	}
	
	public static Type getResolvedSuperclass(Type type)
	{
		Utils.checkNotNull(type, "type");
		
		Class<?> rawType = getErasedReferenceType(type);
		Type supertype = rawType.getGenericSuperclass();
		
		if (supertype == null)
		{
			return null;
		}

		return resolveTypeVariables(supertype, type);
	}
	
	public static Type[] getResolvedInterfaces(Type type)
	{
		Utils.checkNotNull(type, "type");
		
		Class<?> rawType = getErasedReferenceType(type);
		Type[] interfaces = rawType.getGenericInterfaces();
		Type[] resolvedInterfaces = new Type[interfaces.length];
		
		for (int i = 0; i < interfaces.length; i++)
		{
			resolvedInterfaces[i] = resolveTypeVariables(interfaces[i], type);
		}
		
		return resolvedInterfaces;
	}
	
	public static String toString(Type type)
	{
		return toString(type, ClassSerializers.QUALIFIED);
	}
	
	public static String toString(Type type, ClassSerializer serializer)
	{
		if (type instanceof Class<?>)
		{
			Class<?> klass = (Class<?>) type;
			
			if (klass.isArray())
			{
				return toString(klass.getComponentType(), serializer) + "[]";
			}
			
			return serializer.toString(klass);
		}
		
		if (type instanceof TypeVariable<?>)
		{
			return DefaultTypeVariable.toString((TypeVariable<?>) type, serializer);
		}
		
		if (type instanceof GenericArrayType)
		{
			return DefaultGenericArrayType.toString((GenericArrayType) type, serializer);
		}
		
		if (type instanceof ParameterizedType)
		{
			return DefaultParameterizedType.toString((ParameterizedType) type, serializer);
		}
		
		if (type instanceof WildcardType)
		{
			return DefaultWildcardType.toString((WildcardType) type, serializer);
		}
		
		return String.valueOf(type);
	}

	public static String toUnqualifiedString(Type type)
	{
		return toString(type, ClassSerializers.UNQUALIFIED);
	}
	
	public static String toSimpleString(Type type)
	{
		return toString(type, ClassSerializers.SIMPLE);
	}
	
	// package methods --------------------------------------------------------
	
	static StringBuilder appendBounds(StringBuilder builder, Type[] bounds, ClassSerializer serializer)
	{
		for (int i = 0; i < bounds.length; i++)
		{
			if (i > 0)
			{
				builder.append(" & ");
			}
			
			builder.append(toString(bounds[i], serializer));
		}
		
		return builder;
	}
	
	// private methods --------------------------------------------------------
	
	private static void putPrimitiveSubtypes(Map<Class<?>, Set<Class<?>>> subtypesByPrimitive, Class<?> primitiveType,
		Class<?>... directSubtypes)
	{
		Set<Class<?>> subtypes = new HashSet<Class<?>>();
		
		for (Class<?> directSubtype : directSubtypes)
		{
			subtypes.add(directSubtype);
			subtypes.addAll(subtypesByPrimitive.get(directSubtype));
		}
		
		subtypesByPrimitive.put(primitiveType, Collections.unmodifiableSet(subtypes));
	}
	
	private static boolean isClassAssignable(Class<?> supertype, Class<?> type)
	{
		// Class.isAssignableFrom does not perform primitive widening
		if (supertype.isPrimitive() && type.isPrimitive())
		{
			return SUBTYPES_BY_PRIMITIVE.get(supertype).contains(type);
		}
		
		return supertype.isAssignableFrom(type);
	}
	
	private static boolean isParameterizedTypeAssignable(ParameterizedType supertype, ParameterizedType type)
	{
		Type rawSupertype = supertype.getRawType();
		Type rawType = type.getRawType();

		if (!rawSupertype.equals(rawType))
		{
			// short circuit when class raw types are unassignable
			if (rawSupertype instanceof Class<?> && rawType instanceof Class<?>
				&& !(((Class<?>) rawSupertype).isAssignableFrom((Class<?>) rawType)))
			{
				return false;
			}
		
			return isSuperAssignable(supertype, type);
		}
		
		Type[] supertypeArgs = supertype.getActualTypeArguments();
		Type[] typeArgs = type.getActualTypeArguments();
		
		if (supertypeArgs.length != typeArgs.length)
		{
			return false;
		}
		
		for (int i = 0; i < supertypeArgs.length; i++)
		{
			Type supertypeArg = supertypeArgs[i];
			Type typeArg = typeArgs[i];
			
			if (supertypeArg instanceof WildcardType)
			{
				if (!isWildcardTypeAssignable((WildcardType) supertypeArg, typeArg))
				{
					return false;
				}
			}
			else if (!supertypeArg.equals(typeArg))
			{
				return false;
			}
		}
		
		return true;
	}
	
	private static boolean isTypeVariableAssignable(Type supertype, TypeVariable<?> type)
	{
		for (Type bound : type.getBounds())
		{
			if (isAssignable(supertype, bound))
			{
				return true;
			}
		}
		
		return false;
	}
	
	private static boolean isWildcardTypeAssignable(WildcardType supertype, Type type)
	{
		for (Type upperBound : supertype.getUpperBounds())
		{
			if (!isAssignable(upperBound, type))
			{
				return false;
			}
		}
		
		for (Type lowerBound : supertype.getLowerBounds())
		{
			if (!isAssignable(type, lowerBound))
			{
				return false;
			}
		}
		
		return true;
	}
	
	private static boolean isSuperAssignable(Type supertype, Type type)
	{
		Type superclass = getResolvedSuperclass(type);
		
		if (superclass != null && isAssignable(supertype, superclass))
		{
			return true;
		}
		
		for (Type interphace : getResolvedInterfaces(type))
		{
			if (isAssignable(supertype, interphace))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Gets whether the specified type is a <em>reference type</em>.
	 * <p>
	 * More specifically, this method returns {@code true} if the specified type is one of the following:
	 * <ul>
	 * <li>a class type</li>
	 * <li>an interface type</li>
	 * <li>an array type</li>
	 * <li>a parameterized type</li>
	 * <li>a type variable</li>
	 * <li>the null type</li>
	 * </ul>
	 * 
	 * @param type
	 *            the type to check
	 * @return {@code true} if the specified type is a reference type
	 * @see <a href="http://java.sun.com/docs/books/jls/third_edition/html/typesValues.html#4.3">4.3 Reference Types and Values</a>
	 */
	private static boolean isReferenceType(Type type)
	{
		return type == null
			|| type instanceof Class<?>
			|| type instanceof ParameterizedType
			|| type instanceof TypeVariable<?>
			|| type instanceof GenericArrayType;
	}
	
	private static boolean isArraySupertype(Class<?> type)
	{
		return Object.class.equals(type)
			|| Cloneable.class.equals(type)
			|| Serializable.class.equals(type);
	}

	private static Type resolveTypeVariables(Type type, Type subtype)
	{
		// TODO: need to support other types in future, e.g. T[], etc.
		if (!(type instanceof ParameterizedType))
		{
			return type;
		}
		
		Map<Type, Type> actualTypeArgumentsByParameter = getActualTypeArgumentsByParameter(type, subtype);
		Class<?> rawType = getErasedReferenceType(type);
		
		return parameterizeClass(rawType, actualTypeArgumentsByParameter);
	}
	
	private static Map<Type, Type> getActualTypeArgumentsByParameter(Type... types)
	{
		// TODO: return Map<TypeVariable<Class<?>>, Type> somehow
		
		Map<Type, Type> actualTypeArgumentsByParameter = new LinkedHashMap<Type, Type>();
		
		for (Type type : types)
		{
			actualTypeArgumentsByParameter.putAll(getActualTypeArgumentsByParameterInternal(type));
		}
		
		return normalize(actualTypeArgumentsByParameter);
	}
	
	private static Map<Type, Type> getActualTypeArgumentsByParameterInternal(Type type)
	{
		// TODO: look deeply within non-parameterized types when visitors implemented
		if (!(type instanceof ParameterizedType))
		{
			return Collections.emptyMap();
		}
		
		TypeVariable<?>[] typeParameters = getErasedReferenceType(type).getTypeParameters();
		Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
		
		if (typeParameters.length != typeArguments.length)
		{
			throw new MalformedParameterizedTypeException();
		}
		
		Map<Type, Type> actualTypeArgumentsByParameter = new LinkedHashMap<Type, Type>();
		
		for (int i = 0; i < typeParameters.length; i++)
		{
			actualTypeArgumentsByParameter.put(typeParameters[i], typeArguments[i]);
		}
		
		return actualTypeArgumentsByParameter;
	}
	
	private static ParameterizedType parameterizeClass(Class<?> type, Map<Type, Type> actualTypeArgumentsByParameter)
	{
		return parameterizeClassCapture(type, actualTypeArgumentsByParameter);
	}
	
	private static <T> ParameterizedType parameterizeClassCapture(Class<T> type, Map<Type, Type> actualTypeArgumentsByParameter)
	{
		// TODO: actualTypeArgumentsByParameter should be Map<TypeVariable<Class<T>>, Type>
		
		TypeVariable<Class<T>>[] typeParameters = type.getTypeParameters();
		Type[] actualTypeArguments = new Type[typeParameters.length];
		
		for (int i = 0; i < typeParameters.length; i++)
		{
			TypeVariable<Class<T>> typeParameter = typeParameters[i];
			Type actualTypeArgument = actualTypeArgumentsByParameter.get(typeParameter);
			
			if (actualTypeArgument == null)
			{
				throw new IllegalArgumentException("Missing actual type argument for type parameter: " + typeParameter);
			}
			
			actualTypeArguments[i] = actualTypeArgument;
		}
		
		return Types.parameterizedType(getErasedReferenceType(type), actualTypeArguments);
	}
	
	private static <K, V> Map<K, V> normalize(Map<K, V> map)
	{
		// TODO: will this cause an infinite look with recursive bounds?
		
		for (Entry<K, V> entry : map.entrySet())
		{
			K key = entry.getKey();
			V value = entry.getValue();
			
			while (map.containsKey(value))
			{
				value = map.get(value);
			}
			
			map.put(key, value);
		}
		
		return map;
	}
}
