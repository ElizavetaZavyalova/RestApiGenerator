package org.example.read_json.rest_controller_json;

public record JsonKeyWords() {
    public static final String PSEUDONYMS = "pseudonyms";
    public static final String FILTERS = "filters";
    public static final String ADDRESS_PREFIX = "httpPrefix";
    public static final String HTTP = "http";

    public record FilterSuffix() {
        public static final String _AND = ":and";
        public static final String _OR = ":or";
        public static final String _CALL = ":call";
    }

    public record Pseudonym() {
        public static final String TABLES = "tables";
        public static final String FIELDS = "fields";
        public static final String JOINS = "joins";
        public static final String REFS = "refs";
    }
    public record ApplicationProperties(){
        public static final String restApi = "restApi";
        public static final String showSql = "showSql";
        public static String getParamShowSql() {
            return restApi + "." + showSql+":false";
        }
        public record Swagger() {
            public static final String title = "title";
            public static final String openApi = "openApi";
            public static final String description = "description";
            public static final String version = "version";

            public static String getParam(String param) {
                return restApi+"."+openApi + "." + param+":";
            }
        }
    }

    public record Endpoint() {
        public static final String REQUEST = "request";
        public static final String TYPES = "types";
        public static final String TYPE = "type";

        public record Types() {
            public record RequestType() {
                public static final String _GET = "get";
                public static final String _POST = "post";
                public static final String _PUT = "put";
                public static final String _DELETE = "delete";
                public static final String _PATCH = "patch";

            }

            public static final String HTTP_OK = "httpOk";
            public static final String OPERATION = "operation";
            public static final String HTTP_EXCEPTION = "httpException";
            public static final String ENTITY = "entity";

            public static final String FIELDS = "fields";
        }

        public record Request() {
            public static final String AND_ = "&";
            public static final String OR_ = "|";
            public static final String LEFT_BRACKET = "(";
            public static final String RIGHT_BRACKET = ")";
            public static final String LEFT_SQUARE_BRACKET = "[";
            public static final String RIGHT_SQUARE_BRACKET = "]";
            public static final String LEFT_CURLY_BRACKET = "{";
            public static final String RIGHT_CURLY_BRACKET = "}";

            public record TableRef() {
                public static final String _MANY_TO_ONE = ">";
                public static final String _DEFAULT = "";
                public static final String _ONE_TO_MANY = "<";
                public static final String _IN_ONE_WAY = "!";

            }

            public record AggregationFunction() {
                public static final String _MAX = "max_";
                public static final String _MIN = "min_";

            }

            public record Action() {
                public static final String _EQ = "eq";
                public static final String _NE = "ne";
                public static final String _LIKE = "like";
                public static final String _NOT_LIKE = "not_like";
                public static final String _GE = "ge";
                public static final String _GT = "gt";
                public static final String _LE = "le";
                public static final String _LT = "lt";
                public static final String _IN = "in";
                public static final String _DEFAULT="_";

            }

            public record TypeVar() {
                public static final String _STRING = "-s";
                public static final String _INTEGER = "-i";
                public static final String _LONG = "-l";
                public static final String _BOOLEAN = "-b";
            }

        }
    }
}
