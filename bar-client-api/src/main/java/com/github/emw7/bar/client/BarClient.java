package com.github.emw7.bar.client;

import com.github.emw7.bar.logic.api.BarService;
import com.github.emw7.bar.logic.api.error.BarNotFoundException;
import com.github.emw7.bar.model.Bar;
import com.github.emw7.platform.error.RequestErrorException;
import org.springframework.lang.NonNull;

/**
 * The interface to be implemented by service client.
 * <p>
 * Service client is a client that another service can use to access the service.
 */
public interface BarClient extends BarService {

  /**
   * For actual list of thrown exceptions see {@link BarService#create(Bar)}
   * @param bar
   * @return
   */
  @Override
  @NonNull Bar create(@NonNull final Bar bar) throws RequestErrorException;

}
