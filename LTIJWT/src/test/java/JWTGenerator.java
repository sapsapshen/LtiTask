import com.auth0.jwt.algorithms.Algorithm;

import java.util.Calendar;
import java.util.HashMap;

public class JWTGenerator {
    public static final String SECRET = "ASD!@#F^%A";

    public static void main(String[] args) throws Exception {
        HashMap<String, Object> headers = new HashMap<>();
        // 过期时间，6000s
        Calendar expires = Calendar.getInstance();
        expires.add(Calendar.SECOND, 60000);

        String jwtToken = com.auth0.jwt.JWT.create()
                // 第一部分Header
                .withHeader(headers)
                // 第二部分Payload
                .withClaim("name", "Alice Smith")
                .withClaim("email", "alice@example.com")
                .withClaim("roles", "Learner")
                .withClaim("context_id", "course-456")
                .withClaim("context_label", "CS101")
                .withClaim("context_title", "Introduction to Computer Science")
                .withClaim("resource_link_id", "resource-link-uid")
                .withClaim("lti_version", "LTI-1p3")
                .withClaim("lti_message_type", "LTI-Launch")
                .withExpiresAt(expires.getTime())
                // 第三部分Signature
                .sign(Algorithm.HMAC256(SECRET));
        System.out.println(jwtToken);
    }
}
