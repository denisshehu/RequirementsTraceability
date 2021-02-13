package models;

public enum TraceMethod {
    NO_FILTERING, // No filtering.
    SIMILARITY_25, // Similarity of at least 0.25.
    SIMILARITY_67, // Similarity of at least 0.67 of the most similar low level requirement.
    CUSTOM // Our own custom technique. (?)
}
