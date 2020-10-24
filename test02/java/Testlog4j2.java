import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author mhw
 */
public class Testlog4j2
{
    public static void main(String[] args) {
        Logger logger = LogManager.getLogger(Testlog4j2.class);
        logger.trace("trace信息");
        logger.info("info信息");
        logger.debug("debug信息");
        logger.warn("warn信息");
    }
}
