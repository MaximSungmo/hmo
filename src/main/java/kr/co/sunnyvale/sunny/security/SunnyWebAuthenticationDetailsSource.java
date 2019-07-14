package kr.co.sunnyvale.sunny.security;

import java.lang.reflect.Constructor;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * {SpringSecurity}
 * 
 * <p>
 * 로그인 사용자의 웹 디테일 정보 제공 구현체이다.
 * 
 * <p>
 * 로그인 사용자의 웹 디테일 정보는 remoteIP, 세션 아이디, 브라우저 버젼등 클라이언트 정보가 됤 수 있다.
 * 이 구현체는 {@link org.springframework.security.web.authentication.WebAuthenticationDetails}에
 * 위의 정보를 담고 제공하게 된다. 기본 제공 클래스에는 세션아이디, remoteIP 두 개만 제공하기 때문에 상속을 통해 확장할 필요가 있다. 
 * 
 * <p>
 * {@link org.springframework.security.web.authentication.WebAuthenticationDetailsSource}가
 * {@link org.springframework.security.web.authentication.WebAuthenticationDetails} 를 제공하듯
 *
 * 더 많은 정보를 담기위해서는 {@link org.springframework.security.web.authentication.WebAuthenticationDetails}를
 * 확장해야 하고 이 정보를 제공하는 소스클래스도 따로 구현해야 한다.
 * 
 * @author kickscar
 *
 *
 */
public class SunnyWebAuthenticationDetailsSource extends WebAuthenticationDetailsSource {

	private Class<?> clazz = WebAuthenticationDetailsEx.class;

	@Override
	public WebAuthenticationDetails buildDetails( HttpServletRequest context ) {
		Object result = null;
		try {
			Constructor<?> constructor = getFirstMatchingConstructor( context );
			result = constructor.newInstance( context );
		} catch ( Exception ex ) {
			ReflectionUtils.handleReflectionException( ex );
		}
		return ( WebAuthenticationDetails ) result;
	}

	private Constructor<?> getFirstMatchingConstructor( Object object ) throws NoSuchMethodException {
		Constructor<?>[] constructors = clazz.getDeclaredConstructors();
		Constructor<?> constructor = null;

		for ( Constructor<?> tryMe : constructors ) {
			Class<?>[] parameterTypes = tryMe.getParameterTypes();
			if ( parameterTypes.length == 1 && ( object == null || parameterTypes[0].isInstance( object ) ) ) {
				constructor = tryMe;
				break;
			}
		}
		
		
		if( constructor == null ) {
			throw ( object == null ) ?	new NoSuchMethodException( "No constructor found that can take a single argument" ) :
										new NoSuchMethodException( "No constructor found that can take a single argument of type" + object.getClass() );
		}

		return constructor;
	}

	public void setClazz( Class<?> clazz ) {
		Assert.notNull( clazz, "Class required" );
		this.clazz = clazz;
	}
}
