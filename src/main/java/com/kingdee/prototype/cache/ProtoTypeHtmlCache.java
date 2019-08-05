package com.kingdee.prototype.cache;

import com.kingdee.prototype.model.PrototypeHtml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProtoTypeHtmlCache {

    @Autowired
    private SimpleCacheService simpleCacheService;

    private static final String PROTOTYPE_HTML_KEY_ = "PROTOTYPE_HTML_KEY";

    public PrototypeHtml get(String key) {
        return simpleCacheService.hGet(PROTOTYPE_HTML_KEY_, key, PrototypeHtml.class);
    }

    public void save(String key, PrototypeHtml prototypeHtml) {
        simpleCacheService.hAdd(PROTOTYPE_HTML_KEY_, key, prototypeHtml);
    }

    public List<PrototypeHtml> getAll() {
        Map<String, PrototypeHtml> map = simpleCacheService.hGetAll(PROTOTYPE_HTML_KEY_, PrototypeHtml.class);
        List<PrototypeHtml> collect = map.values().stream().sorted((p1, p2) ->
                (int) (p1.getCreateTime().getTime() - p2.getCreateTime().getTime())
        ).collect(Collectors.toList());
        return collect;
    }
}
