/* Generated By:JJTree: Do not edit this line. AstNotEqual.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=Ast,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.wxxr.mobile.el.parser;

public
class AstNotEqual extends SimpleNode {
  public AstNotEqual(int id) {
    super(id);
  }

  public AstNotEqual(ELParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ELParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=7011562a6cb1ab4b003ad4381f05c502 (do not edit this line) */