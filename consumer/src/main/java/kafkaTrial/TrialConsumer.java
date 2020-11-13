package kafkaTrial;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.Executors;

import static java.lang.String.format;
import static java.util.Collections.singleton;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

public class TrialConsumer {

    public static final Properties properties;

    public static final String GROUP_NAME = "trialGroup";

    static {
        properties = new Properties();
        properties.put(BOOTSTRAP_SERVERS_CONFIG, ClusterProperties.CLUSTER_LOCATION);
        properties.put(KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");

    }

    public static void main(String[] args) throws Exception {
        properties.put(GROUP_ID_CONFIG, args.length == 0 ? GROUP_NAME : args[0]);
        System.out.println("Eventually groupName="+properties.getProperty(GROUP_ID_CONFIG));
        Consumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(singleton(ClusterProperties.CLUSTER_TYPE));
        Executors.callable((Runnable) () -> {
            System.out.println("waiting for messages...");
            System.out.println(format("%10s %10s %10s %10s", "topic", "partition", "key", "value"));
            while (true) {
                ConsumerRecords<String, String> messageBundle = consumer.poll(Duration.ofMillis(100));
                if (messageBundle.count() > 0) {
                    System.out.println(format("%d new messages...", messageBundle.count()));
                    messageBundle.iterator().forEachRemaining(record ->
                            System.out.println(format("%10s %10s %10s %10s", record.topic(), record.partition(), record.key(), record.value())));
                }
            }
        }).call();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (!br.readLine().equals("exit")) {
            Thread.sleep(333);
        }
        consumer.close();
    }
}
