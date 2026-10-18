package zw.ac.msu.dzimbahub.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import zw.ac.msu.dzimbahub.model.DemoRecord;
import zw.ac.msu.dzimbahub.repository.DemoRecordRepository;
import zw.ac.msu.dzimbahub.repository.PropertyRepository;

@RestController 
@RequestMapping("/api") 
public class DemoController {
    private final DemoRecordRepository repo; 
    private final PropertyRepository properties; 

    public DemoController(DemoRecordRepository r, PropertyRepository p){
        repo=r;
        properties=p;
    }

    private Map<String,Object> save(String type,Map<String,Object>b){
        DemoRecord r=new DemoRecord();
        r.setType(type);
        Object pid=b.get("propertyId");
        if(pid instanceof Number) r.setPropertyId(((Number)pid).longValue());
        r.setName(String.valueOf(b.getOrDefault("name","Demo Tenant")));
        if(b.get("rating") instanceof Number) r.setRating(((Number)b.get("rating")).intValue());
        r.setContent(String.valueOf(b.getOrDefault("message",b.getOrDefault("comment",b))));
        repo.save(r);
        return Map.of("id",r.getId(),"message","Saved successfully");
    }

    @PostMapping("/viewings") 
    public Map<String,Object> viewing(@RequestBody Map<String,Object>b){
        return save("viewing",b);
    } 
    
    @PostMapping("/messages") 
    public Map<String,Object> message(@RequestBody Map<String,Object>b){
        return save("message",b);
    } 
    
    @PostMapping("/reviews") 
    public Map<String,Object> review(@RequestBody Map<String,Object>b){
        return save("review",b);
    }

    // FIXED: Changed from /admin/stats to /demo/stats
    @GetMapping("/demo/stats") 
    public Map<String,Long> stats(){
        return Map.of(
            "properties",properties.count(),
            "viewings",repo.countByType("viewing"),
            "messages",repo.countByType("message"),
            "reviews",repo.countByType("review")
        );
    }
}