package com.github.valchevgd.view;

public enum ColorTheme {
    LIGHT,
    DEFAULT,
    DARK;

    public static String getCssPath(ColorTheme colorTheme) {

        switch (colorTheme) {
            case DARK:
                return "css/themeDark.css";
            case DEFAULT:
                return "css/themeDefault.css";
            case LIGHT:
                return "css/themeLight";
            default:
                return null;
        }
    }
}
