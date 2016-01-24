package com.kuo.urcoco.common.item;

/**
 * Created by User on 2015/11/28.
 */
public class TypeItem {

    private String typeKind;
    private String typeName;
    private String typePath;
    private int typeColor;

    public void setTypeColor(int typeColor) {
        this.typeColor = typeColor;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setTypePath(String typePath) {
        this.typePath = typePath;
    }

    public void setTypeKind(String typeKind) {
        this.typeKind = typeKind;
    }

    public String getTypeKind() {
        return typeKind;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getTypeColor() {
        return typeColor;
    }

    public String getTypePath() {
        return typePath;
    }
}
