package kafkaTrial;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.stream.IntStream;

import static kafkaTrial.ClusterProperties.CLUSTER_LOCATION;
import static kafkaTrial.ClusterProperties.CLUSTER_TYPE;
import static org.apache.kafka.clients.producer.ProducerConfig.*;

public class TrialProducer {

    public static final Properties properties;

    static {
        properties = new Properties();
        properties.put(BOOTSTRAP_SERVERS_CONFIG, CLUSTER_LOCATION);
        properties.put(KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");

    }

    public static void main(String[] args) {
        Producer<String, String> producer = new KafkaProducer<>(properties);
        String message = args.length > 0 ? args[0] : CLUSTER_TYPE;
        IntStream.range(0, 9).forEach(number -> {
            producer.send(new ProducerRecord<>(CLUSTER_TYPE, Integer.toString(number), message + number));
            producer.flush();
        });
        producer.close();

    }
}
