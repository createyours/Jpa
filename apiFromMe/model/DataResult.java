package org.leadingsoft.golf.api.model;

import org.leadingsoft.golf.api.code.ResultCode;

/**
 * The result of request. Which will be built as response body.
 *
 * @param <T> the type of content
 */
public class DataResult<T> {

  public DataResult() {
    super();
    this.message = "";
    this.statusCode = ResultCode.OK.code();
  }

  /**
   * the status code in result
   */
  private String statusCode;
  /**
   * the message in result
   */
  private String message;
  /**
   * the content of result
   */
  private T content;

  /**
   * Constructor Which will create a normal result.
   *
   * @param content response data
   */
  public DataResult(T content) {
    this.statusCode = ResultCode.OK.code();
    this.content = content;
    this.message = "";
  }

  /**
   * Constructor Which will create a simple result using parameter statusCode and message. It will
   * not have content.
   *
   * @param statusCode the status code of result
   * @param message the message of result
   */
  public DataResult(String statusCode, String message) {
    this.statusCode = statusCode;
    this.message = message;
    this.content = null;
  }

  /**
   * Constructor Which will create a simple result using parameter statusCode and message and
   * content
   *
   * @param statusCode the status code of result
   * @param message the message of result
   * @param content the browser language
   */
  public DataResult(String statusCode, String message, T content) {
    this.statusCode = statusCode;
    this.message = message;
    this.content = content;
  }

  /**
   * Get the status code
   *
   * @return the value of statusCode
   */
  public String getStatusCode() {
    return statusCode;
  }

  /**
   * Get the message
   *
   * @return the value of message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Get the content
   *
   * @return the value of content
   */
  public T getContent() {
    return content;
  }
}
