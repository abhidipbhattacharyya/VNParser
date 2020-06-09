package com.mycompany.app.datastructures;
import java.util.*;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
@Setter
@Getter
@Accessors(fluent = true)

public class BadDataInfo{

    int jsonId;
    List<Integer> sentenceId = new ArrayList<>();

    public BadDataInfo()
    {

    }

    public BadDataInfo(int jsonId)
    {
        this.jsonId = jsonId;
    }

    public void addSentenceId(int id)
    {
      sentenceId.add(id);
    }

    public int size()
    {
      return sentenceId.size();
    }
}
