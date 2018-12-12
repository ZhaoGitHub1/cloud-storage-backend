package cn.com.yizhu.common.handler;


import cn.com.yizhu.common.exception.BizException;
import cn.com.yizhu.common.vo.ResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/**
 * 全局异常拦截
 */
@ControllerAdvice
public class WebExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG= LoggerFactory.getLogger(WebExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOG.error("请求类型不匹配：",ex);
        return new ResponseEntity<Object>(ResponseVO.FAIL_MSG,HttpStatus.NOT_EXTENDED);

    }

    @ResponseBody
    @ExceptionHandler(value = BizException.class)
    public ResponseVO bizExceptionHandler(Exception e){
        LOG.error("业务处理异常：",e);
        return ResponseVO.fileResponseVO(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseVO exceptionHandler(Exception e){
        LOG.error("拦截到异常：",e);
        return ResponseVO.fileResponseVO(e.getMessage());
    }
}
