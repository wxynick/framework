package com.wxxr.mobile.dao;

import java.util.List;

import com.wxxr.mobile.dao.internal.TableStatements;

import android.database.Cursor;

/** For internal use by greenDAO only. */
public final class InternalQueryDaoAccess<T> {
    private final AbstractDao<T, ?> dao;

    public InternalQueryDaoAccess(AbstractDao<T, ?> abstractDao) {
        dao = abstractDao;
    }

    public T loadCurrent(Cursor cursor, int offset, boolean lock) {
        return dao.loadCurrent(cursor, offset, lock);
    }

    public List<T> loadAllAndCloseCursor(Cursor cursor) {
        return dao.loadAllAndCloseCursor(cursor);
    }

    public T loadUniqueAndCloseCursor(Cursor cursor) {
        return dao.loadUniqueAndCloseCursor(cursor);
    }

    public TableStatements getStatements() {
        return dao.getStatements();
    }

    public static <T2> TableStatements getStatements(AbstractDao<T2, ?> dao) {
        return dao.getStatements();
    }

}