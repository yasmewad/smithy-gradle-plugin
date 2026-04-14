$version: "2"

namespace com.example

service EchoService {
    version: "1.0"
    operations: [Echo]
}

operation Echo {
    input := {
        message: String
    }
    output := {
        message: String
    }
}
