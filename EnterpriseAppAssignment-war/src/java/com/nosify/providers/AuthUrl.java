package com.nosify.providers;

import java.util.HashMap;
import java.util.List;

public interface AuthUrl {
    public List<String> signInUrls();
    public HashMap<String, List<String>> roleUrls();
}
