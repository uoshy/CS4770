package chat;

import java.util.Date;
import java.io.Serializable;

public class Phrase implements Serializable {
    private String host = null;
    private String speaker = "unknown";
    private String phrase;
    private Date date = new Date();

    public Phrase( String phrase ) {
        this.phrase = phrase;
    }

    public Phrase( String speaker, String phrase ) {
        this( phrase );
        this.speaker = speaker;
    }

    public Phrase( String speaker, String phrase, String host ) {
        this( speaker, phrase );
        this.host = host;
    }

    public Phrase( String speaker, String phrase, String host, Date date ) {
        this( speaker, phrase, host );
        this.date = date;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append( date.toString() );
        if ( speaker != null && !speaker.equals( "unknown" ) ) {
            sb.append( ":" );
            sb.append( speaker );
        }
        if ( host != null  ) {
            sb.append( ":" );
            sb.append( host );
        }
        if ( phrase != null  ) {
            sb.append( ":" );
            sb.append( phrase );
        }
        return sb.toString();
    }

    public String getHost() {
        return host;
    }

    public String getSpeaker() {
        return speaker;
    }

    public String getPhrase() {
        return phrase;
    }

    public Date getDate() {
        return date;
    }

    public static void main( String[] args ) {
        Phrase ph = new Phrase( "rod", "hi." );
        System.out.println( ph );
    }
}
