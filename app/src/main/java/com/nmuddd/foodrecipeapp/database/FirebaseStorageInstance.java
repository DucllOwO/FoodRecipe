package com.nmuddd.foodrecipeapp.database;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseStorageInstance {
    public static final String STORAGE_PATH_UPLOADS = "Images/";
    public final StorageReference firebaseStorage = FirebaseStorage.getInstance().getReference();
}
