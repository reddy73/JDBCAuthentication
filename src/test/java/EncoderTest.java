import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncoderTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder= new BCryptPasswordEncoder();
        System.out.println(encoder.encode("P@ssw0rd"));
    }
}
