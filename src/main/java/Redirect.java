

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;

import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

/**
 * Servlet implementation class Redirect
 */
@WebServlet("/Redirect")
public class Redirect extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String Client_id = "1006671225011-7r6k144jul8dcmd2m8kq1e1dngf46nrr.apps.googleusercontent.com";
    String Client_secret = "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";
    String redirect_url="http://localhost:8080/Oauth/Redirect";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Redirect() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String code = request.getParameter("code");
		OAuth20Service service = new ServiceBuilder(Client_id)
                .apiSecret(Client_secret)
                .callback(redirect_url)
                .defaultScope("profile email")
                .build(GoogleApi20.instance());

        String authorizationUrl = service.getAuthorizationUrl();
        response.sendRedirect(authorizationUrl);
        
        
        try {
			OAuth2AccessToken accessToken = service.getAccessToken(code);
            OAuthRequest req = new OAuthRequest(Verb.GET, "https://www.googleapis.com/oauth2/v2/userinfo");
            service.signRequest(accessToken, req);
            Response res = service.execute(req);

            JSONObject json = new JSONObject(res.getBody());
            String email = json.getString("email");
            String name = json.getString("name");
		} catch (IOException | InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
