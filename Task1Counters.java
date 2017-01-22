package C10TODO;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.Map;

public class Task1Counters implements Counters{

    Map<String, LongAdder> viewCount = new ConcurrentHashMap<String, LongAdder>();

    @Override
    public void increment (String tag){
        viewCount.putIfAbsent(tag, new LongAdder());
        viewCount.get(tag).increment();
    }

    @Override
    public Map<String, Long> getCountersAndClear(){
        Map<String, Long> result = new HashMap<>();
        viewCount.forEach((key, value) -> result.put(key, value.sumThenReset()));
        return result;
    }
}
