package au.ryanlea.amazon;

import com.amazonaws.services.simpleemail.AWSJavaMailTransport;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.annotation.PostConstruct;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import java.util.Properties;

public class AmazonMailSender extends JavaMailSenderImpl {

    public static final String MAIL_TRANSPORT_PROTOCOL_KEY = "mail.transport.protocol";

    private String awsAccessKeyId;

    private String awsSecretKey;

    public void setAwsAccessKeyId(String awsAccessKeyId) {
        this.awsAccessKeyId = awsAccessKeyId;
    }

    public void setAwsSecretKey(String awsSecretKey) {
        this.awsSecretKey = awsSecretKey;
    }

    @PostConstruct
    public void init() {
        Properties props = getJavaMailProperties();
        props.setProperty(MAIL_TRANSPORT_PROTOCOL_KEY, "aws");
        props.setProperty(AWSJavaMailTransport.AWS_ACCESS_KEY_PROPERTY, awsAccessKeyId);
        props.setProperty(AWSJavaMailTransport.AWS_SECRET_KEY_PROPERTY, awsSecretKey);

        // set port to -1 to ensure that spring calls the equivalent of transport.connect().
        setPort(-1);
    }

    @Override
    protected Transport getTransport(Session session) throws NoSuchProviderException {
        return new AWSJavaMailTransport(session, null);
    }
}
