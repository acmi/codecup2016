/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.validator.routines;

import java.io.Serializable;
import java.net.IDN;
import java.util.Arrays;
import java.util.Locale;
import org.apache.commons.validator.routines.RegexValidator;

public class DomainValidator
implements Serializable {
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private final boolean allowLocal;
    private static final DomainValidator DOMAIN_VALIDATOR = new DomainValidator(false);
    private static final DomainValidator DOMAIN_VALIDATOR_WITH_LOCAL = new DomainValidator(true);
    private final RegexValidator domainRegex = new RegexValidator("^(?:\\p{Alnum}(?>[\\p{Alnum}-]{0,61}\\p{Alnum})?\\.)+(\\p{Alpha}(?>[\\p{Alnum}-]{0,61}\\p{Alnum})?)\\.?$");
    private final RegexValidator hostnameRegex = new RegexValidator("\\p{Alnum}(?>[\\p{Alnum}-]{0,61}\\p{Alnum})?");
    private static final String[] INFRASTRUCTURE_TLDS = new String[]{"arpa"};
    private static final String[] GENERIC_TLDS = new String[]{"aaa", "aarp", "abb", "abbott", "abbvie", "abogado", "abudhabi", "academy", "accenture", "accountant", "accountants", "aco", "active", "actor", "adac", "ads", "adult", "aeg", "aero", "afl", "agakhan", "agency", "aig", "airforce", "airtel", "akdn", "alibaba", "alipay", "allfinanz", "ally", "alsace", "amica", "amsterdam", "analytics", "android", "anquan", "apartments", "app", "apple", "aquarelle", "aramco", "archi", "army", "arte", "asia", "associates", "attorney", "auction", "audi", "audio", "author", "auto", "autos", "avianca", "aws", "axa", "azure", "baby", "baidu", "band", "bank", "bar", "barcelona", "barclaycard", "barclays", "barefoot", "bargains", "bauhaus", "bayern", "bbc", "bbva", "bcg", "bcn", "beats", "beer", "bentley", "berlin", "best", "bet", "bharti", "bible", "bid", "bike", "bing", "bingo", "bio", "biz", "black", "blackfriday", "bloomberg", "blue", "bms", "bmw", "bnl", "bnpparibas", "boats", "boehringer", "bom", "bond", "boo", "book", "boots", "bosch", "bostik", "bot", "boutique", "bradesco", "bridgestone", "broadway", "broker", "brother", "brussels", "budapest", "bugatti", "build", "builders", "business", "buy", "buzz", "bzh", "cab", "cafe", "cal", "call", "camera", "camp", "cancerresearch", "canon", "capetown", "capital", "car", "caravan", "cards", "care", "career", "careers", "cars", "cartier", "casa", "cash", "casino", "cat", "catering", "cba", "cbn", "ceb", "center", "ceo", "cern", "cfa", "cfd", "chanel", "channel", "chase", "chat", "cheap", "chloe", "christmas", "chrome", "church", "cipriani", "circle", "cisco", "citic", "city", "cityeats", "claims", "cleaning", "click", "clinic", "clinique", "clothing", "cloud", "club", "clubmed", "coach", "codes", "coffee", "college", "cologne", "com", "commbank", "community", "company", "compare", "computer", "comsec", "condos", "construction", "consulting", "contact", "contractors", "cooking", "cool", "coop", "corsica", "country", "coupon", "coupons", "courses", "credit", "creditcard", "creditunion", "cricket", "crown", "crs", "cruises", "csc", "cuisinella", "cymru", "cyou", "dabur", "dad", "dance", "date", "dating", "datsun", "day", "dclk", "dealer", "deals", "degree", "delivery", "dell", "deloitte", "delta", "democrat", "dental", "dentist", "desi", "design", "dev", "diamonds", "diet", "digital", "direct", "directory", "discount", "dnp", "docs", "dog", "doha", "domains", "download", "drive", "dubai", "durban", "dvag", "earth", "eat", "edeka", "edu", "education", "email", "emerck", "energy", "engineer", "engineering", "enterprises", "epson", "equipment", "erni", "esq", "estate", "eurovision", "eus", "events", "everbank", "exchange", "expert", "exposed", "express", "extraspace", "fage", "fail", "fairwinds", "faith", "family", "fan", "fans", "farm", "fashion", "fast", "feedback", "ferrero", "film", "final", "finance", "financial", "firestone", "firmdale", "fish", "fishing", "fit", "fitness", "flickr", "flights", "florist", "flowers", "flsmidth", "fly", "foo", "football", "ford", "forex", "forsale", "forum", "foundation", "fox", "fresenius", "frl", "frogans", "frontier", "ftr", "fund", "furniture", "futbol", "fyi", "gal", "gallery", "gallo", "gallup", "game", "garden", "gbiz", "gdn", "gea", "gent", "genting", "ggee", "gift", "gifts", "gives", "giving", "glass", "gle", "global", "globo", "gmail", "gmbh", "gmo", "gmx", "gold", "goldpoint", "golf", "goo", "goog", "google", "gop", "got", "gov", "grainger", "graphics", "gratis", "green", "gripe", "group", "gucci", "guge", "guide", "guitars", "guru", "hamburg", "hangout", "haus", "hdfcbank", "health", "healthcare", "help", "helsinki", "here", "hermes", "hiphop", "hitachi", "hiv", "hockey", "holdings", "holiday", "homedepot", "homes", "honda", "horse", "host", "hosting", "hoteles", "hotmail", "house", "how", "hsbc", "htc", "hyundai", "ibm", "icbc", "ice", "icu", "ifm", "iinet", "imamat", "immo", "immobilien", "industries", "infiniti", "info", "ing", "ink", "institute", "insurance", "insure", "int", "international", "investments", "ipiranga", "irish", "iselect", "ismaili", "ist", "istanbul", "itau", "iwc", "jaguar", "java", "jcb", "jcp", "jetzt", "jewelry", "jlc", "jll", "jmp", "jnj", "jobs", "joburg", "jot", "joy", "jpmorgan", "jprs", "juegos", "kaufen", "kddi", "kerryhotels", "kerrylogistics", "kerryproperties", "kfh", "kia", "kim", "kinder", "kitchen", "kiwi", "koeln", "komatsu", "kpmg", "kpn", "krd", "kred", "kuokgroup", "kyoto", "lacaixa", "lamborghini", "lamer", "lancaster", "land", "landrover", "lanxess", "lasalle", "lat", "latrobe", "law", "lawyer", "lds", "lease", "leclerc", "legal", "lexus", "lgbt", "liaison", "lidl", "life", "lifeinsurance", "lifestyle", "lighting", "like", "limited", "limo", "lincoln", "linde", "link", "live", "living", "lixil", "loan", "loans", "locus", "lol", "london", "lotte", "lotto", "love", "ltd", "ltda", "lupin", "luxe", "luxury", "madrid", "maif", "maison", "makeup", "man", "management", "mango", "market", "marketing", "markets", "marriott", "mba", "med", "media", "meet", "melbourne", "meme", "memorial", "men", "menu", "meo", "miami", "microsoft", "mil", "mini", "mls", "mma", "mobi", "mobily", "moda", "moe", "moi", "mom", "monash", "money", "montblanc", "mormon", "mortgage", "moscow", "motorcycles", "mov", "movie", "movistar", "mtn", "mtpc", "mtr", "museum", "mutual", "mutuelle", "nadex", "nagoya", "name", "natura", "navy", "nec", "net", "netbank", "network", "neustar", "new", "news", "nexus", "ngo", "nhk", "nico", "nikon", "ninja", "nissan", "nissay", "nokia", "northwesternmutual", "norton", "nowruz", "nra", "nrw", "ntt", "nyc", "obi", "office", "okinawa", "omega", "one", "ong", "onl", "online", "ooo", "oracle", "orange", "org", "organic", "origins", "osaka", "otsuka", "ovh", "page", "pamperedchef", "panerai", "paris", "pars", "partners", "parts", "party", "passagens", "pet", "pharmacy", "philips", "photo", "photography", "photos", "physio", "piaget", "pics", "pictet", "pictures", "pid", "pin", "ping", "pink", "pizza", "place", "play", "playstation", "plumbing", "plus", "pohl", "poker", "porn", "post", "praxi", "press", "pro", "prod", "productions", "prof", "progressive", "promo", "properties", "property", "protection", "pub", "pwc", "qpon", "quebec", "quest", "racing", "read", "realtor", "realty", "recipes", "red", "redstone", "redumbrella", "rehab", "reise", "reisen", "reit", "ren", "rent", "rentals", "repair", "report", "republican", "rest", "restaurant", "review", "reviews", "rexroth", "rich", "ricoh", "rio", "rip", "rocher", "rocks", "rodeo", "room", "rsvp", "ruhr", "run", "rwe", "ryukyu", "saarland", "safe", "safety", "sakura", "sale", "salon", "samsung", "sandvik", "sandvikcoromant", "sanofi", "sap", "sapo", "sarl", "sas", "saxo", "sbi", "sbs", "sca", "scb", "schaeffler", "schmidt", "scholarships", "school", "schule", "schwarz", "science", "scor", "scot", "seat", "security", "seek", "select", "sener", "services", "seven", "sew", "sex", "sexy", "sfr", "sharp", "shaw", "shell", "shia", "shiksha", "shoes", "shouji", "show", "shriram", "sina", "singles", "site", "ski", "skin", "sky", "skype", "smile", "sncf", "soccer", "social", "softbank", "software", "sohu", "solar", "solutions", "song", "sony", "soy", "space", "spiegel", "spot", "spreadbetting", "srl", "stada", "star", "starhub", "statebank", "statefarm", "statoil", "stc", "stcgroup", "stockholm", "storage", "store", "stream", "studio", "study", "style", "sucks", "supplies", "supply", "support", "surf", "surgery", "suzuki", "swatch", "swiss", "sydney", "symantec", "systems", "tab", "taipei", "talk", "taobao", "tatamotors", "tatar", "tattoo", "tax", "taxi", "tci", "team", "tech", "technology", "tel", "telecity", "telefonica", "temasek", "tennis", "teva", "thd", "theater", "theatre", "tickets", "tienda", "tiffany", "tips", "tires", "tirol", "tmall", "today", "tokyo", "tools", "top", "toray", "toshiba", "total", "tours", "town", "toyota", "toys", "trade", "trading", "training", "travel", "travelers", "travelersinsurance", "trust", "trv", "tube", "tui", "tunes", "tushu", "tvs", "ubs", "unicom", "university", "uno", "uol", "vacations", "vana", "vegas", "ventures", "verisign", "versicherung", "vet", "viajes", "video", "vig", "viking", "villas", "vin", "vip", "virgin", "vision", "vista", "vistaprint", "viva", "vlaanderen", "vodka", "volkswagen", "vote", "voting", "voto", "voyage", "vuelos", "wales", "walter", "wang", "wanggou", "watch", "watches", "weather", "weatherchannel", "webcam", "weber", "website", "wed", "wedding", "weibo", "weir", "whoswho", "wien", "wiki", "williamhill", "win", "windows", "wine", "wme", "wolterskluwer", "work", "works", "world", "wtc", "wtf", "xbox", "xerox", "xihuan", "xin", "xn--11b4c3d", "xn--1ck2e1b", "xn--1qqw23a", "xn--30rr7y", "xn--3bst00m", "xn--3ds443g", "xn--3pxu8k", "xn--42c2d9a", "xn--45q11c", "xn--4gbrim", "xn--55qw42g", "xn--55qx5d", "xn--5tzm5g", "xn--6frz82g", "xn--6qq986b3xl", "xn--80adxhks", "xn--80asehdb", "xn--80aswg", "xn--8y0a063a", "xn--9dbq2a", "xn--9et52u", "xn--9krt00a", "xn--b4w605ferd", "xn--bck1b9a5dre4c", "xn--c1avg", "xn--c2br7g", "xn--cck2b3b", "xn--cg4bki", "xn--czr694b", "xn--czrs0t", "xn--czru2d", "xn--d1acj3b", "xn--eckvdtc9d", "xn--efvy88h", "xn--estv75g", "xn--fct429k", "xn--fhbei", "xn--fiq228c5hs", "xn--fiq64b", "xn--fjq720a", "xn--flw351e", "xn--g2xx48c", "xn--gckr3f0f", "xn--hxt814e", "xn--i1b6b1a6a2e", "xn--imr513n", "xn--io0a7i", "xn--j1aef", "xn--jlq61u9w7b", "xn--jvr189m", "xn--kcrx77d1x4a", "xn--kpu716f", "xn--kput3i", "xn--mgba3a3ejt", "xn--mgbab2bd", "xn--mgbb9fbpob", "xn--mgbca7dzdo", "xn--mgbt3dhd", "xn--mk1bu44c", "xn--mxtq1m", "xn--ngbc5azd", "xn--ngbe9e0a", "xn--nqv7f", "xn--nqv7fs00ema", "xn--nyqy26a", "xn--p1acf", "xn--pbt977c", "xn--pssy2u", "xn--q9jyb4c", "xn--qcka1pmc", "xn--rhqv96g", "xn--rovu88b", "xn--ses554g", "xn--t60b56a", "xn--tckwe", "xn--unup4y", "xn--vermgensberater-ctb", "xn--vermgensberatung-pwb", "xn--vhquv", "xn--vuq861b", "xn--w4r85el8fhu5dnra", "xn--xhq521b", "xn--zfr164b", "xperia", "xxx", "xyz", "yachts", "yahoo", "yamaxun", "yandex", "yodobashi", "yoga", "yokohama", "you", "youtube", "yun", "zara", "zero", "zip", "zone", "zuerich"};
    private static final String[] COUNTRY_CODE_TLDS = new String[]{"ac", "ad", "ae", "af", "ag", "ai", "al", "am", "ao", "aq", "ar", "as", "at", "au", "aw", "ax", "az", "ba", "bb", "bd", "be", "bf", "bg", "bh", "bi", "bj", "bm", "bn", "bo", "br", "bs", "bt", "bv", "bw", "by", "bz", "ca", "cc", "cd", "cf", "cg", "ch", "ci", "ck", "cl", "cm", "cn", "co", "cr", "cu", "cv", "cw", "cx", "cy", "cz", "de", "dj", "dk", "dm", "do", "dz", "ec", "ee", "eg", "er", "es", "et", "eu", "fi", "fj", "fk", "fm", "fo", "fr", "ga", "gb", "gd", "ge", "gf", "gg", "gh", "gi", "gl", "gm", "gn", "gp", "gq", "gr", "gs", "gt", "gu", "gw", "gy", "hk", "hm", "hn", "hr", "ht", "hu", "id", "ie", "il", "im", "in", "io", "iq", "ir", "is", "it", "je", "jm", "jo", "jp", "ke", "kg", "kh", "ki", "km", "kn", "kp", "kr", "kw", "ky", "kz", "la", "lb", "lc", "li", "lk", "lr", "ls", "lt", "lu", "lv", "ly", "ma", "mc", "md", "me", "mg", "mh", "mk", "ml", "mm", "mn", "mo", "mp", "mq", "mr", "ms", "mt", "mu", "mv", "mw", "mx", "my", "mz", "na", "nc", "ne", "nf", "ng", "ni", "nl", "no", "np", "nr", "nu", "nz", "om", "pa", "pe", "pf", "pg", "ph", "pk", "pl", "pm", "pn", "pr", "ps", "pt", "pw", "py", "qa", "re", "ro", "rs", "ru", "rw", "sa", "sb", "sc", "sd", "se", "sg", "sh", "si", "sj", "sk", "sl", "sm", "sn", "so", "sr", "st", "su", "sv", "sx", "sy", "sz", "tc", "td", "tf", "tg", "th", "tj", "tk", "tl", "tm", "tn", "to", "tr", "tt", "tv", "tw", "tz", "ua", "ug", "uk", "us", "uy", "uz", "va", "vc", "ve", "vg", "vi", "vn", "vu", "wf", "ws", "xn--3e0b707e", "xn--45brj9c", "xn--80ao21a", "xn--90a3ac", "xn--90ais", "xn--clchc0ea0b2g2a9gcd", "xn--d1alf", "xn--e1a4c", "xn--fiqs8s", "xn--fiqz9s", "xn--fpcrj9c3d", "xn--fzc2c9e2c", "xn--gecrj9c", "xn--h2brj9c", "xn--j1amh", "xn--j6w193g", "xn--kprw13d", "xn--kpry57d", "xn--l1acc", "xn--lgbbat1ad8j", "xn--mgb9awbf", "xn--mgba3a4f16a", "xn--mgbaam7a8h", "xn--mgbayh7gpa", "xn--mgbbh1a71e", "xn--mgbc0a9azcg", "xn--mgberp4a5d4ar", "xn--mgbpl2fh", "xn--mgbtx2b", "xn--mgbx4cd0ab", "xn--mix891f", "xn--node", "xn--o3cw4h", "xn--ogbpf8fl", "xn--p1ai", "xn--pgbs0dh", "xn--qxam", "xn--s9brj9c", "xn--wgbh1c", "xn--wgbl6a", "xn--xkc2al3hye2a", "xn--xkc2dl3a5ee0h", "xn--y9a3aq", "xn--yfro4i67o", "xn--ygbi2ammx", "ye", "yt", "za", "zm", "zw"};
    private static final String[] LOCAL_TLDS = new String[]{"localdomain", "localhost"};
    private static boolean inUse = false;
    private static volatile String[] countryCodeTLDsPlus = EMPTY_STRING_ARRAY;
    private static volatile String[] genericTLDsPlus = EMPTY_STRING_ARRAY;
    private static volatile String[] countryCodeTLDsMinus = EMPTY_STRING_ARRAY;
    private static volatile String[] genericTLDsMinus = EMPTY_STRING_ARRAY;

    public static synchronized DomainValidator getInstance(boolean bl) {
        inUse = true;
        if (bl) {
            return DOMAIN_VALIDATOR_WITH_LOCAL;
        }
        return DOMAIN_VALIDATOR;
    }

    private DomainValidator(boolean bl) {
        this.allowLocal = bl;
    }

    public boolean isValid(String string) {
        if (string == null) {
            return false;
        }
        if ((string = DomainValidator.unicodeToASCII(string)).length() > 253) {
            return false;
        }
        String[] arrstring = this.domainRegex.match(string);
        if (arrstring != null && arrstring.length > 0) {
            return this.isValidTld(arrstring[0]);
        }
        return this.allowLocal && this.hostnameRegex.isValid(string);
    }

    public boolean isValidTld(String string) {
        string = DomainValidator.unicodeToASCII(string);
        if (this.allowLocal && this.isValidLocalTld(string)) {
            return true;
        }
        return this.isValidInfrastructureTld(string) || this.isValidGenericTld(string) || this.isValidCountryCodeTld(string);
    }

    public boolean isValidInfrastructureTld(String string) {
        String string2 = this.chompLeadingDot(DomainValidator.unicodeToASCII(string).toLowerCase(Locale.ENGLISH));
        return DomainValidator.arrayContains(INFRASTRUCTURE_TLDS, string2);
    }

    public boolean isValidGenericTld(String string) {
        String string2 = this.chompLeadingDot(DomainValidator.unicodeToASCII(string).toLowerCase(Locale.ENGLISH));
        return (DomainValidator.arrayContains(GENERIC_TLDS, string2) || DomainValidator.arrayContains(genericTLDsPlus, string2)) && !DomainValidator.arrayContains(genericTLDsMinus, string2);
    }

    public boolean isValidCountryCodeTld(String string) {
        String string2 = this.chompLeadingDot(DomainValidator.unicodeToASCII(string).toLowerCase(Locale.ENGLISH));
        return (DomainValidator.arrayContains(COUNTRY_CODE_TLDS, string2) || DomainValidator.arrayContains(countryCodeTLDsPlus, string2)) && !DomainValidator.arrayContains(countryCodeTLDsMinus, string2);
    }

    public boolean isValidLocalTld(String string) {
        String string2 = this.chompLeadingDot(DomainValidator.unicodeToASCII(string).toLowerCase(Locale.ENGLISH));
        return DomainValidator.arrayContains(LOCAL_TLDS, string2);
    }

    private String chompLeadingDot(String string) {
        if (string.startsWith(".")) {
            return string.substring(1);
        }
        return string;
    }

    static String unicodeToASCII(String string) {
        if (DomainValidator.isOnlyASCII(string)) {
            return string;
        }
        try {
            String string2 = IDN.toASCII(string);
            if (IDN_TOASCII_PRESERVES_TRAILING_DOTS) {
                return string2;
            }
            int n2 = string.length();
            if (n2 == 0) {
                return string;
            }
            char c2 = string.charAt(n2 - 1);
            switch (c2) {
                case '.': 
                case '\u3002': 
                case '\uff0e': 
                case '\uff61': {
                    return string2 + ".";
                }
            }
            return string2;
        }
        catch (IllegalArgumentException illegalArgumentException) {
            return string;
        }
    }

    private static boolean isOnlyASCII(String string) {
        if (string == null) {
            return true;
        }
        for (int i2 = 0; i2 < string.length(); ++i2) {
            if (string.charAt(i2) <= '') continue;
            return false;
        }
        return true;
    }

    private static boolean arrayContains(String[] arrstring, String string) {
        return Arrays.binarySearch(arrstring, string) >= 0;
    }

    private static class IDNBUGHOLDER {
        private static final boolean IDN_TOASCII_PRESERVES_TRAILING_DOTS = IDNBUGHOLDER.keepsTrailingDot();

        private static boolean keepsTrailingDot() {
            String string = "a.";
            return "a.".equals(IDN.toASCII("a."));
        }
    }

}

