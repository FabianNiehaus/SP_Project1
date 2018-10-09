class ShadowEntry{
    String username;
    String salt;
    String hash;

    ShadowEntry(String username, String salt, String hash) {
        this.username = username;
        this.salt = salt;
        this.hash = hash;
    }
}