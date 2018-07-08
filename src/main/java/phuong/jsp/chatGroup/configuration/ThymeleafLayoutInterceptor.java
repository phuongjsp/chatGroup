package phuong.jsp.chatGroup.configuration;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

public class ThymeleafLayoutInterceptor extends HandlerInterceptorAdapter {

    private static final String DEFAULT_VIEW_ATTRIBUTE_NAME = "view";

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {


        if ( modelAndView == null || !modelAndView.hasView () ) {
            return;
        }

        String originalViewName = modelAndView.getViewName ();
        if ( isRedirectOrForward (originalViewName) ) {
            return;
        }
        Map<String, String> layout = getLayoutName (handler);
        if ( layout == null ) return;

        modelAndView.setViewName (layout.get ("value"));
        request.setAttribute ("title", layout.get ("title"));
        modelAndView.addObject (DEFAULT_VIEW_ATTRIBUTE_NAME, originalViewName);
    }

    private boolean isRedirectOrForward(String viewName) {
        return viewName.startsWith ("redirect:") || viewName.startsWith ("forward:");
    }

    private Map<String, String> getLayoutName(Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Layout layout = handlerMethod.getMethodAnnotation (Layout.class);
        if ( layout == null ) {
            return null;
        } else {
            Map<String, String> map = new LinkedHashMap<> ();
            map.put ("value", layout.value ());
            map.put ("title", layout.title ());
            return map;
        }
    }
}