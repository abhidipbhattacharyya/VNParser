package com.mycompany.app.utils;




public class StringUtils
{
  public static String ljust(String s, int n) {
     return String.format("%-" + n + "s", s);
   }

   public static String rjust(String s, int n) {
     return String.format("%" + n + "s", s);
  }
}
