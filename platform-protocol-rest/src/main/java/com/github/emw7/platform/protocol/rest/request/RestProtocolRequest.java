package com.github.emw7.platform.protocol.rest.request;

import com.github.emw7.platform.protocol.api.ProtocolRequest;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;

public interface RestProtocolRequest<B> extends ProtocolRequest<B> {

  @NonNull
  HttpMethod getHttpMethod();

  @NonNull
  HttpHeaders getHttpHeaders();

  @NonNull
  List<MediaType> acceptableMediaTypes ();

  /**
   * {@code @Nullable} because not all the methods have the body and thus the content type.
   *
   * @return the type of the body; {@code null} is the method does not accept body
   */
  @Nullable
  MediaType contentType ();

  @NonNull
  Map<String, Object> getPathParams();

  @NonNull
  MultiValueMap<String, String> getQueryParams();

//  @NonNull
//  Pageable getPageable();
  @NonNull Pagination getPagination ();

  @Nullable
  B getBody();

  // //////////
  class Pagination {
    private final Pageable pageable;

    private final String pageNumberParamName;
    private final String pageSizeParamName;
    private final String sortParamName;
    private final String sortPropertyDirectionSep;

    private Pagination(final Pageable pageable, final String pageNumber, final String pageSize,
        final String sort,
        final String sortPropertyDirectionSep) {
      this.pageable = pageable;
      this.pageNumberParamName = pageNumber;
      this.pageSizeParamName = pageSize;
      this.sortParamName = sort;
      this.sortPropertyDirectionSep = sortPropertyDirectionSep;
    }

    public Pageable getPageable() {
      return pageable;
    }

    public String getPageNumberParamName() {
      return pageNumberParamName;
    }

    public String getPageSizeParamName() {
      return pageSizeParamName;
    }

    public String getSortParamName() {
      return sortParamName;
    }

    public String getSortPropertyDirectionSep() {
      return sortPropertyDirectionSep;
    }

    public static class Builder {
      private Builder () {}

      private Pageable pageable= Pageable.unpaged();

      private String pageNumberParamName= "page";
      private String pageSizeParamName= "size";
      private String sortParamName= "sort";
      private final String sortPropertyDirectionSep= ",'";

      public Builder pageable (@NonNull final Pageable pageable) {
        this.pageable= pageable;
        return this;
      }

      public Builder pageNumberParamName (@NonNull final String pageNumberParamName) {
        this.pageNumberParamName= pageNumberParamName;
        return this;
      }

      public Builder pageSizeParamName (@NonNull final String pageSizeParamName) {
        this.pageSizeParamName= pageSizeParamName;
        return this;
      }

      public Builder sortParamName (@NonNull final String sortParamName) {
        this.sortParamName= sortParamName;
        return this;
      }

      public Pagination build () {
        return new Pagination(pageable, pageNumberParamName, pageSizeParamName, sortParamName, sortPropertyDirectionSep);
      }

    }

    public static Builder builder() { return new Builder(); }
  }
  // //////////
}
