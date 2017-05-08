package xyz.a5s7.hotel.rating;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.a5s7.hotel.rating.domain.DomainParser;
import xyz.a5s7.hotel.rating.domain.Hotel;
import xyz.a5s7.hotel.rating.domain.Review;
import xyz.a5s7.hotel.rating.domain.Semantics;
import xyz.a5s7.hotel.rating.domain.SemanticsParser;
import xyz.a5s7.hotel.rating.nlp.SynonymsMap;
import xyz.a5s7.hotel.rating.search.NaiveTopicSearcher;
import xyz.a5s7.hotel.rating.search.TopicSearcher;
import xyz.a5s7.hotel.rating.stats.Statistics;

import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

public class HotelRating {
    private static final Logger log = LoggerFactory.getLogger(HotelRating.class);

    private static class Wrapper {
        Hotel hotel;
        double score;

        public Wrapper(final Hotel hotel, final double score) {
            this.hotel = hotel;
            this.score = score;
        }

        @Override
        public String toString() {
            return "{hotel=" + hotel + ", score=" + score + '}';
        }
    }

    public static void main(String[] args) throws IOException {
        CommandLineParser parser = new PosixParser();
        Options options = new Options();
        options.addOption(new Option("h", "help", false, "Print this message" ));
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine parsedArgs = parser.parse(options, args);

            if (parsedArgs.hasOption("h")) {
                printHelp(options, formatter);
                return;
            }

            List argList = parsedArgs.getArgList();
            if (argList == null || argList.size() != 2) {
                printHelp(options, formatter);
                return;
            }

            final String topic = (String) argList.get(0);
            final String path = (String) argList.get(1);

            File[] reviews = getReviews(path);

            rate(reviews, topic);
        } catch (ParseException e) {
            System.err.println("Couldn't parse command line arguments");
            System.exit(1);
        }
    }

    private static void printHelp(final Options options, final HelpFormatter formatter) {
        formatter.printHelp( "hotel_rating [ topic ] [ directory ]" + System.lineSeparator(),
                "Rate hotes for topic" + System.lineSeparator(),
                options, "");
    }

    private static void rate(final File[] reviews, final String topic) throws IOException {
        DomainParser domainParser = new DomainParser();
        SynonymsMap synonymsMap = new SynonymsMap();

        Semantics semantics = new SemanticsParser().fromJson(
                HotelRating.class.getResourceAsStream("/semantics.json")
        );
        TopicSearcher topicSearcher = new NaiveTopicSearcher();

        TreeSet<Wrapper> sortedHotels = new TreeSet<>((wrapper, t1) -> wrapper.score <= t1.score ? 1 : -1);
        for (File review : reviews) {
            try {
                Hotel hotel = domainParser.fromJson(review);
                log.debug("=================================================================================");
                Collection<Review> reviewCollection = hotel.getReviews();
                log.debug("Rating hotel {} based on {} reviews", hotel, reviewCollection.size());
                List<String> synonyms = synonymsMap.findSynonyms(topic);
                Collection<Statistics> statistics = topicSearcher.findReviewsForTopic(synonyms, reviewCollection, semantics);
                double v = statistics.stream().mapToDouble(st -> st.getScore()).average().orElse(0);
                sortedHotels.add(new Wrapper(hotel, v));
                log.debug("Hotel {} rated {}", hotel, v);
                log.debug("Topic {} found in {} reviews for hotel {}. It rated {}", topic, statistics.size(), hotel, v);
                log.debug("=================================================================================");
            } catch (IOException e) {
                System.out.println("Unable to parse file " + review.getAbsolutePath());
            }
        }

        sortedHotels.forEach(wrapper ->
            log.info("Rating for hotel {} is {}", wrapper.hotel, wrapper.score)
        );
    }

    private static File[] getReviews(String path) {
        File dir = new File(path);
        return dir.listFiles((dir1, name) -> name.contains("review"));
    }
}
