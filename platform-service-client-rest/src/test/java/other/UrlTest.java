package other;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.lang.NonNull;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Disabled
public class UrlTest {

  @Test
  public void url ()
  {
    Pageable p= PageRequest.of(1,10, Sort.by(Order.desc("test"),
        Order.asc("x")));
    Map<String,Object> pp= Map.of("id","1+2%4&5");
    MultiValueMap<String, String> qp= new LinkedMultiValueMap<>();
    qp.add("test","uno/1");
    qp.add("test","due&2");
    qp.add("x","1 00%");
    UriComponentsBuilder ucb= UriComponentsBuilder
        .fromHttpUrl(
            "https://foo.micro-services.lan")
        .pathSegment("foo")
        .pathSegment("v1")
        .path("do/this")
        .pathSegment(
            pp.keySet().stream().map(k -> '{' + k + '}').collect(Collectors.toList()).toArray(new String[0]))
        .queryParams(qp);
//    qp.entrySet().stream().forEach( e -> ucb.queryParam(e.getKey(), e.getValue().stream().map( v -> URLEncoder.encode(v,
//        StandardCharsets.UTF_8)).collect(Collectors.toList())));
    UriComponents uc = ucb.buildAndExpand(pp);
        //.queryParams(qp.entrySet().stream().)
        //.buildAndExpand(pp);
  String url= uc.encode().toUriString();
  System.out.println("url: " + url);
  }

  private MultiValueMap<String, String> pageable (MultiValueMap<String, String> params, @NonNull final Pageable pageable)
  {
    String page = String.valueOf(pageable.getPageNumber());
    params.put("page", Collections.singletonList(page));
    String size = String.valueOf(pageable.getPageSize());
    params.put("size", Collections.singletonList(size));

    Sort sort = pageable.getSort();
    StringBuilder sortParamsBuilder= new StringBuilder();
    List<String> sortParams = new ArrayList<>();
    for(Order order: sort) {
      sortParamsBuilder.setLength(0);
      sortParamsBuilder.append(order.getProperty()).append(",").append(order.getDirection());
      sortParams.add(sortParamsBuilder.toString());
    }
    params.put("sort", sortParams);

    return params;
  }

}
