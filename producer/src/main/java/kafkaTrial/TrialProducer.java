package kafkaTrial;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.stream.IntStream;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

public class TrialProducer {

    public static final Properties properties;

    static {
        properties = new Properties();
        properties.put(BOOTSTRAP_SERVERS_CONFIG, ClusterProperties.CLUSTER_LOCATION);
        properties.put(KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");

    }

    public static void main(String[] args) {
        Producer<String, String> producer = new KafkaProducer<>(properties);
        IntStream.range(0, 9).forEach(number -> {
            producer.send(new ProducerRecord<>(ClusterProperties.CLUSTER_TYPE, Integer.toString(number), ClusterProperties.CLUSTER_TYPE + number));
            producer.flush();
        });
        producer.close();

    }
}
