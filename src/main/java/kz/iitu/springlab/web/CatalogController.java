package kz.iitu.springlab.web;

import kz.iitu.springlab.aspect.SlowMethodAspect;
import kz.iitu.springlab.service.CatalogService;
import org.springframework.aop.support.AopUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lab4")
public class CatalogController {

    private final CatalogService catalogService;
    private final SlowMethodAspect slowMethodAspect;

    public CatalogController(CatalogService catalogService, SlowMethodAspect slowMethodAspect) {
        this.catalogService = catalogService;
        this.slowMethodAspect = slowMethodAspect;
    }

    @GetMapping("/item/{id}")
    public String item(@PathVariable long id) {
        return catalogService.findById(id);
    }

    @GetMapping("/items")
    public List<String> items(@RequestParam(defaultValue = "5") int limit) {
        return catalogService.findAll(limit);
    }

    @DeleteMapping("/item/{id}")
    public String remove(@PathVariable long id) {
        return catalogService.remove(id);
    }

    @GetMapping("/proxy")
    public Map<String, String> proxyInfo() {
        return Map.of(
            "className",  catalogService.getClass().getName(),
            "superClass", catalogService.getClass().getSuperclass().getSimpleName(),
            "isAopProxy", String.valueOf(AopUtils.isAopProxy(catalogService)),
            "isCglib",    String.valueOf(AopUtils.isCglibProxy(catalogService)));
    }

    @GetMapping("/remove-twice/{id}")
    public String removeTwice(@PathVariable long id) {
        return catalogService.removeTwice(id);
    }

    @GetMapping("/remove-twice-fixed/{id}")
    public String removeTwiceFixed(@PathVariable long id) {
        return catalogService.removeTwiceFixed(id);
    }

    // variant 2: the list of slow calls collected by SlowMethodAspect
    @GetMapping("/slow-calls")
    public Map<String, Object> slowCalls() {
        List<SlowMethodAspect.SlowCall> calls = slowMethodAspect.getSlowCalls();
        return Map.of(
            "thresholdMs", slowMethodAspect.getThresholdMs(),
            "count",       calls.size(),
            "calls",       calls);
    }
}
