package com.mycompany.app.utils;

import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
@Setter
@Getter
@Accessors(fluent = true)

public class Counter{
  HashMap<String, Integer> counter = new HashMap<>();

  public int get(String key)
  {
    if(counter.containsKey(key))
      return counter.get(key).intValue();
    return 0;
  }


  public void put(String key, int val)
  {
    counter.put(key, val);
  }

  public boolean containsKey(String key)
  {
    return counter.containsKey(key);
  }

  public void update(String key, int count)
  {
    int oldCount = get(key);
    put(key, oldCount + count);
  }

  public void update(List<String> keys)
  {
    for( String key: keys)
    {
        update(key,1);
    }
  }

  private HashMap mostCommon() //soring by value
  {
       List list = new LinkedList(counter.entrySet());
       // Defined Custom Comparator here
       Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
               return ((Comparable) ((Map.Entry) (o2)).getValue())
                  .compareTo(((Map.Entry) (o1)).getValue());
            }
       });

       // Here I am copying the sorted list in HashMap
       // using LinkedHashMap to preserve the insertion order
       HashMap sortedHashMap = new LinkedHashMap();
       for (Iterator it = list.iterator(); it.hasNext();) {
              Map.Entry entry = (Map.Entry) it.next();
              sortedHashMap.put(entry.getKey(), entry.getValue());
       }
       return sortedHashMap;
  }

  public JSONObject getJsonObject()
  {
      counter = mostCommon();
      //System.out.println(counter);
      JSONObject label = new JSONObject();
      for (Map.Entry mapElement : counter.entrySet())
      {
          String key  = (String)mapElement.getKey();
          int value = (int)mapElement.getValue();
          //System.out.println(key+":"+value);
          label.put(key, value);
      }
    //  System.out.println("-------------------------");
      //label = mostCommon(label);
      //System.out.println(label);
      return label;
  }
}
