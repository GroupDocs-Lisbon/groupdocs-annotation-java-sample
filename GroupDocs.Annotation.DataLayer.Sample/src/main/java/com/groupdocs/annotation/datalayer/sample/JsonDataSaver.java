package com.groupdocs.annotation.datalayer.sample;

import com.groupdocs.annotation.contracts.DocumentMetadata;
import com.groupdocs.annotation.contracts.IAnnotationDataLayer;
import com.groupdocs.annotation.contracts.Role;
import com.groupdocs.annotation.contracts.dataobjects.AnnotationDataObject;
import com.groupdocs.annotation.contracts.dataobjects.AnnotationReplyDataObject;
import com.groupdocs.annotation.contracts.dataobjects.CollaboratorDataObject;
import com.groupdocs.annotation.contracts.dataobjects.UserDataObject;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class JsonDataSaver implements IAnnotationDataLayer {

    static String storagePath;

    @Override
    public boolean deleteDocument(String fileId) throws Exception {
        File folder = new File(storagePath + "Documents\\" + fileId);
        if (!folder.exists()) {
            return true;
        }

        delete(folder);
        return true;
    }

    @Override
    public DocumentMetadata getDocumentMetadata(String fileId) throws Exception {
        String descPath = storagePath + "Documents\\" + fileId + "\\Description.json";
        DocumentMetadata documentMetadata;
        Gson gson = new Gson();
        try (BufferedReader br = new BufferedReader(new FileReader(descPath))) {
            documentMetadata = gson.fromJson(br, DocumentMetadata.class);
        }

        return documentMetadata;
    }

    @Override
    public ArrayList<DocumentMetadata> getDocumentsInfo() throws Exception {
        Gson gson = new Gson();
        ArrayList<DocumentMetadata> documentsData = new ArrayList<>();
        ArrayList<File> folders = new ArrayList<>();
        File folder = new File(storagePath + "Documents\\");
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                folders.add(fileEntry);
            }
        }
        for (File doc : folders) {
            try (BufferedReader br = new BufferedReader(new FileReader(doc + "\\Description.json"))) {
                DocumentMetadata documentMetadata = gson.fromJson(br, DocumentMetadata.class);
                documentsData.add(documentMetadata);
            }
        }
        return documentsData;
    }

    @Override
    public void setStoragePath(String storagePath) {
        JsonDataSaver.storagePath = storagePath;
    }

    @Override
    public String saveDocument(InputStream documentStream, DocumentMetadata documentInfo) throws Exception {
        documentInfo.setDocumentId(UUID.randomUUID().toString());

        String docPath = storagePath + "Documents\\" + documentInfo.getDocumentId();
        String fileDescriptionName = "Description.json";

        // Save document
        String path = docPath + "\\" + documentInfo.getName();
        File file = new File(path);
        file.getParentFile().mkdirs();
        file.createNewFile();

        OutputStream out = new FileOutputStream(file);
        byte[] b = new byte[documentStream.available()];
        documentStream.read(b);
        out.write(b);
        // Save document description
        String serealiazedDescription;
        try (PrintWriter writer = new PrintWriter(docPath + "/" + fileDescriptionName, "UTF-8")) {
            Gson gson = new GsonBuilder().serializeNulls().create();
            serealiazedDescription = gson.toJson(documentInfo);
            writer.print(serealiazedDescription);
        }
        return documentInfo.getDocumentId();
    }

    @Override
    public ByteArrayOutputStream getClearDocument(String documentId) throws Exception {
        String path = storagePath + "Documents\\" + documentId;
        FileInputStream document;
        DocumentMetadata documentMetadata = this.getDocumentMetadata(documentId);

        try {
            document = new FileInputStream(path + "\\" + documentMetadata.getName());
        } catch (Exception e) {
            throw new Exception("File reading exception");
        }

        ByteArrayOutputStream tempDocument = new ByteArrayOutputStream();

        byte[] b = new byte[document.available()];
        document.read(b);
        tempDocument.write(b);
        document.close();
        return tempDocument;
    }

    @Override
    public String createAnnotation(String documentId, AnnotationDataObject newAnnotation) throws IOException {
        String path = storagePath + "Documents\\" + documentId;
        TreeMap<String, TreeMap<Integer, AnnotationDataObject>> annotationsDictionary = getDictionaryFromFile(documentId);
        if (annotationsDictionary == null) {
            annotationsDictionary = new TreeMap<>();
        }
        TreeMap<Integer, AnnotationDataObject> versionDictionary = new TreeMap<>();
        versionDictionary.put(newAnnotation.getVersion(), newAnnotation);
        annotationsDictionary.put(newAnnotation.getId(), versionDictionary);
        Gson gson = new GsonBuilder().serializeNulls().create();
        String annotationJson = gson.toJson(annotationsDictionary);
        try (BufferedWriter output = new BufferedWriter(new FileWriter(path + "\\" + "Annotations.json"))) {
            output.write(annotationJson);
        }

        return newAnnotation.getId();

    }

    @Override
    public ArrayList<AnnotationDataObject> getDocumentAnnotations(String documentId) throws IOException {
        Map<String, TreeMap<Integer, AnnotationDataObject>> annotationsDictionary = getDictionaryFromFile(documentId);
        if (annotationsDictionary == null) {
            return null;
        }
        ArrayList<AnnotationDataObject> annotations = new ArrayList<>();

        for (Map.Entry<String, TreeMap<Integer, AnnotationDataObject>> entry : annotationsDictionary.entrySet()) {
            TreeMap<Integer, AnnotationDataObject> versionMap = entry.getValue();
            AnnotationDataObject annotation = versionMap.lastEntry().getValue();
            annotations.add(annotation);
        }
        return annotations;
    }

    @Override
    public AnnotationDataObject getDocumentAnnotation(String documentId, String annotationId, int version) throws IOException {
        TreeMap<String, TreeMap<Integer, AnnotationDataObject>> annotationsDictionary = getDictionaryFromFile(documentId);
        TreeMap<Integer, AnnotationDataObject> versionList = annotationsDictionary.get(annotationId);
        if (versionList.containsKey(version)) {
            return versionList.get(version);
        }

        return versionList.lastEntry().getValue();
    }

    @Override
    public AnnotationDataObject editAnnotation(String documentId, AnnotationDataObject annotation) throws IOException {
        String path = storagePath + "Documents\\" + documentId;
        TreeMap<String, TreeMap<Integer, AnnotationDataObject>> annotationsDictionary = getDictionaryFromFile(documentId);
        if (annotationsDictionary == null) {
            annotationsDictionary = new TreeMap<>();
        }

        TreeMap<Integer, AnnotationDataObject> versionDictionary = annotationsDictionary.get(annotation.getId());
        versionDictionary.put(annotation.getVersion(), annotation);
        annotationsDictionary.put(annotation.getId(), versionDictionary);

        Gson gson = new GsonBuilder().serializeNulls().create();
        String annotationJson = gson.toJson(annotationsDictionary);
        try (BufferedWriter output = new BufferedWriter(new FileWriter(new File(path + "\\Annotations.json")))) {
            output.write(annotationJson);
        }

        return annotation;
    }

    @Override
    public boolean deleteAnnotation(String documentId, AnnotationDataObject annotation) throws IOException {
        String path = storagePath + "Documents\\" + documentId;
        TreeMap<String, TreeMap<Integer, AnnotationDataObject>> annotationsDictionary = getDictionaryFromFile(documentId);
        if (annotationsDictionary == null) {
            annotationsDictionary = new TreeMap<>();
        }

        TreeMap<Integer, AnnotationDataObject> versionDictionary = annotationsDictionary.get(annotation.getId());
        versionDictionary.put(annotation.getVersion(), annotation);
        annotationsDictionary.put(annotation.getId(), versionDictionary);
        Gson gson = new GsonBuilder().serializeNulls().create();
        String annotationJson = gson.toJson(annotationsDictionary);
        try (BufferedWriter output = new BufferedWriter(new FileWriter(new File(path + "\\Annotations.json")))) {
            output.write(annotationJson);
        } catch (IOException e) {
        }
        return true;
    }

    @Override
    public String createAnnotationReply(String documentId, String annotationId, AnnotationReplyDataObject reply) throws IOException {
        String path = storagePath + "Documents\\" + documentId;
        TreeMap<String, TreeMap<Integer, AnnotationDataObject>> annotationsDictionary = getDictionaryFromFile(documentId);
        if (annotationsDictionary == null) {
            annotationsDictionary = new TreeMap<>();
        }

        TreeMap<Integer, AnnotationDataObject> versionDictionary = annotationsDictionary.get(annotationId);
        AnnotationDataObject annotation = versionDictionary.lastEntry().getValue();
        if (annotation.getAnnotationReplies() == null) {
            annotation.setAnnotationReplies(new ArrayList<AnnotationReplyDataObject>());
        }

        // Add reply to annotation
        annotation.getAnnotationReplies().add(reply);
        annotationsDictionary.put(annotationId, versionDictionary);

        // Save new collection to the file
        Gson gson = new GsonBuilder().serializeNulls().create();
        String annotationJson = gson.toJson(annotationsDictionary);
        try (BufferedWriter output = new BufferedWriter(new FileWriter(new File(path + "\\Annotations.json")))) {
            output.write(annotationJson);
        } catch (IOException e) {
        }

        return reply.getId();
    }

    @Override
    public ArrayList<AnnotationReplyDataObject> getAnnotationReplies(String documentId, String annotationId) throws IOException {
        TreeMap<String, TreeMap<Integer, AnnotationDataObject>> annotationsDictionary = getDictionaryFromFile(documentId);
        TreeMap<Integer, AnnotationDataObject> versionDictionary = annotationsDictionary.get(annotationId);
        AnnotationDataObject annotation = versionDictionary.lastEntry().getValue();
        return annotation.getAnnotationReplies() == null ? new ArrayList<AnnotationReplyDataObject>() : annotation.getAnnotationReplies();
    }

    @Override
    public AnnotationReplyDataObject getAnnotationReply(String documentId, String annotationId, String replyId) throws IOException {
        TreeMap<String, TreeMap<Integer, AnnotationDataObject>> annotationsDictionary = getDictionaryFromFile(documentId);
        TreeMap<Integer, AnnotationDataObject> versionDictionary = annotationsDictionary.get(annotationId);
        AnnotationDataObject annotation = versionDictionary.lastEntry().getValue();
        if (annotation.getAnnotationReplies().isEmpty()) {
            return null;
        } else {
            for (AnnotationReplyDataObject reply : annotation.getAnnotationReplies()) {
                if (reply.getId().equals(replyId)) {
                    return reply;
                }
            }
        }
        return null;
    }

    @Override
    public AnnotationReplyDataObject editAnnotationReply(String documentId, String annotationId, AnnotationReplyDataObject reply) throws IOException {
        String path = storagePath + "Documents\\" + documentId;
        TreeMap<String, TreeMap<Integer, AnnotationDataObject>> annotationsDictionary = getDictionaryFromFile(documentId);
        TreeMap<Integer, AnnotationDataObject> versionDictionary = annotationsDictionary.get(annotationId);
        AnnotationDataObject annotation = versionDictionary.lastEntry().getValue();

        ArrayList<AnnotationReplyDataObject> replies = annotation.getAnnotationReplies();
        if (replies == null) {
            return null;
        }

        ArrayList<AnnotationReplyDataObject> newReplies = new ArrayList<>();
        for (AnnotationReplyDataObject tmpReply : replies) {
            if (!tmpReply.getId().equals(reply.getId())) {
                newReplies.add(tmpReply);
            }
        }
        annotation.setAnnotationReplies(newReplies);

        versionDictionary.put(annotation.getVersion(), annotation);
        annotationsDictionary.put(annotation.getId(), versionDictionary);
        Gson gson = new GsonBuilder().serializeNulls().create();
        String annotationJson = gson.toJson(annotationsDictionary);
        try (BufferedWriter output = new BufferedWriter(new FileWriter(new File(path + "\\Annotations.json")))) {
            output.write(annotationJson);
        } catch (IOException e) {
        }

        return reply;
    }

    @Override
    public boolean deleteAnnotationReply(String documentId, String annotationId, String annotationReplyId) throws IOException {
        String path = storagePath + "Documents\\" + documentId;
        TreeMap<String, TreeMap<Integer, AnnotationDataObject>> annotationsDictionary = getDictionaryFromFile(documentId);
        TreeMap<Integer, AnnotationDataObject> versionDictionary = annotationsDictionary.get(annotationId);
        AnnotationDataObject annotation = versionDictionary.lastEntry().getValue();
        ArrayList<AnnotationReplyDataObject> replies = annotation.getAnnotationReplies();
        // Delete old reply
        ArrayList<AnnotationReplyDataObject> newReplies = new ArrayList<>();
        for (AnnotationReplyDataObject tmpReply : replies) {
            if (!tmpReply.getId().equals(annotationReplyId)) {
                newReplies.add(tmpReply);
            }
        }
        annotation.setAnnotationReplies(newReplies);
        versionDictionary.put(annotation.getVersion(), annotation);
        annotationsDictionary.put(annotation.getId(), versionDictionary);
        Gson gson = new GsonBuilder().serializeNulls().create();
        String annotationJson = gson.toJson(annotationsDictionary);
        try (BufferedWriter output = new BufferedWriter(new FileWriter(new File(path + "\\Annotations.json")))) {
            output.write(annotationJson);
        } catch (IOException e) {
        }

        return true;
    }

    @Override
    public ArrayList<CollaboratorDataObject> getDocumentCollaborators(String documentId) throws Exception {
        DocumentMetadata documentMetadata = getDocumentMetadata(documentId);
        return documentMetadata.getCollaborators();
    }

    @Override
    public CollaboratorDataObject editDocumentCollaborator(String documentId, CollaboratorDataObject collaborator) throws Exception {
        DocumentMetadata documentMetadata = getDocumentMetadata(documentId);
        ArrayList<CollaboratorDataObject> oldCollaborators = documentMetadata.getCollaborators();
        ArrayList<CollaboratorDataObject> newCollaborators = new ArrayList<>();
        for (CollaboratorDataObject tmpCollaborator : oldCollaborators) {
            if (!tmpCollaborator.getId().equals(collaborator.getId())) {
                newCollaborators.add(tmpCollaborator);
            }
        }

        newCollaborators.add(collaborator);
        documentMetadata.setCollaborators(newCollaborators);
        Gson gson = new GsonBuilder().serializeNulls().create();
        try (BufferedWriter br = new BufferedWriter(new FileWriter(storagePath + "/Documents/" + documentId + "/Description.json"))) {
            br.write(gson.toJson(documentMetadata, DocumentMetadata.class));
        } catch (IOException e) {
        }
        return collaborator;
    }

    @Override
    public String addDocumentCollaborator(String documentId, CollaboratorDataObject collaborator) throws Exception {
        DocumentMetadata documentMetadata = getDocumentMetadata(documentId);
        ArrayList<CollaboratorDataObject> collaborators = documentMetadata.getCollaborators();
        collaborators.add(collaborator);
        documentMetadata.setCollaborators(collaborators);
        Gson gson = new GsonBuilder().serializeNulls().create();
        try (BufferedWriter br = new BufferedWriter(new FileWriter(storagePath + "/Documents/" + documentId + "/Description.json"))) {
            br.write(gson.toJson(documentMetadata, DocumentMetadata.class));
        } catch (IOException e) {
        }
        return collaborator.getId();
    }

    @Override
    public boolean removeDocumentCollaborator(String documentId, String collaboratorId) throws Exception {
        DocumentMetadata documentMetadata = getDocumentMetadata(documentId);
        ArrayList<CollaboratorDataObject> oldCollaborators = documentMetadata.getCollaborators();
        ArrayList<CollaboratorDataObject> newCollaborators = new ArrayList<>();
        for (CollaboratorDataObject tmpCollaborator : oldCollaborators) {
            if (!tmpCollaborator.getId().equals(collaboratorId)) {
                newCollaborators.add(tmpCollaborator);
            }
        }

        documentMetadata.setCollaborators(newCollaborators);
        Gson gson = new GsonBuilder().serializeNulls().create();
        try (BufferedWriter br = new BufferedWriter(new FileWriter(storagePath + "/Documents/" + documentId + "/Description.json"))) {
            br.write(gson.toJson(documentMetadata, DocumentMetadata.class));
        } catch (IOException e) {
        }
        return true;
    }

    @Override
    public ArrayList<Role> getRoles() throws Exception {
        String path = storagePath;
        File file = new File(path + "\\Roles.json");
        if (!file.exists()) {
            file.createNewFile();
            return null;
        }
        ArrayList<Role> roles = null;
        Type type = new TypeToken<ArrayList<Role>>() {
        }.getType();
        Gson gson = new Gson();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            roles = gson.fromJson(br, type);
        }
        return roles;
    }

    @Override
    public ArrayList<String> getRolePermissions(String roleName) throws IOException {
        String path = storagePath;
        File file = new File(path + "\\Roles.json");
        if (!file.exists()) {
            file.createNewFile();
            return null;
        }

        ArrayList<Role> roles;
        Type type = new TypeToken<ArrayList<Role>>() {
        }.getType();
        Gson gson = new Gson();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            roles = gson.fromJson(br, type);
        }
        Role role = null;
        for (Role tmpRole : roles) {
            if (tmpRole.getName().equals(roleName)) {
                role = tmpRole;
                break;
            }
        }

        return role == null ? new ArrayList<String>() : role.getPermissions();
    }

    @Override
    public String createUserRole(Role role) throws IOException {
        String path = storagePath;
        File file = new File(path + "\\Roles.json");
        if (!file.exists()) {
            file.createNewFile();
            return null;
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        ArrayList<Role> roles = null;
        Type type = new TypeToken<ArrayList<Role>>() {
        }.getType();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            roles = gson.fromJson(br, type);
        }

        if (roles == null) {
            roles = new ArrayList<>();
        }

        for (Role tmpRole : roles) {
            if (tmpRole.getName().equals(role.getName())) {
                return role.getName();
            }
        }

        roles.add(role);

        try (BufferedWriter br = new BufferedWriter(new FileWriter(path + "\\Roles.json"))) {
            br.write(gson.toJson(roles, type));
        } catch (IOException e) {
        }

        return role.getName();
    }

    @Override
    public boolean addRoleToUser(String roleName, String userId) throws IOException {
        UserDataObject user = getUser(userId);
        user.getRoles().add(roleName);
        editUser(user);
        return true;
    }

    @Override
    public boolean deleteRoleFromUser(String roleName, String userId) throws IOException {
        UserDataObject user = getUser(userId);
        ArrayList<String> roles = new ArrayList<>();
        for (String role : user.getRoles()) {
            if (!role.equals(roleName)) {
                roles.add(role);
            }
        }
        user.setRoles(roles);
        editUser(user);
        return true;
    }

    @Override
    public boolean addRolePermission(String role, String permission) throws IOException {
        String path = storagePath;
        File file = new File(path + "\\Roles.json");
        if (!file.exists()) {
            file.createNewFile();
            return false;
        }

        Gson gson = new GsonBuilder().serializeNulls().create();
        ArrayList<Role> roles = null;
        Type type = new TypeToken<ArrayList<Role>>() {
        }.getType();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            roles = gson.fromJson(br, type);
        }

        if (roles == null) {
            roles = new ArrayList<>();
        }

        Role oldRole = new Role();
        for (Role tmpRole : roles) {
            if (tmpRole.getName().equals(role)) {
                oldRole = tmpRole;
                break;
            }
        }

        oldRole.getPermissions().add(permission);

        ArrayList<Role> newRoles = new ArrayList<>();
        for (Role tmpRole : roles) {
            if (!tmpRole.getName().equals(oldRole.getName())) {
                newRoles.add(tmpRole);
            }
        }

        newRoles.add(oldRole);
        try (BufferedWriter br = new BufferedWriter(new FileWriter(path + "\\Roles.json"))) {
            br.write(gson.toJson(newRoles, type));
        } catch (IOException e) {
        }

        return true;
    }

    @Override
    public boolean removeRolePermission(String role, String permission) throws IOException {
        String path = storagePath;
        File file = new File(path + "\\Roles.json");
        if (!file.exists()) {
            file.createNewFile();
            return false;
        }

        Gson gson = new GsonBuilder().serializeNulls().create();
        ArrayList<Role> roles = null;
        Type type = new TypeToken<ArrayList<Role>>() {
        }.getType();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            roles = gson.fromJson(br, type);
        } catch (IOException e) {
        }

        if (roles == null) {
            return false;
        }
        //search role in object roles
        Role oldRole = new Role();
        for (Role tmpRole : roles) {
            if (tmpRole.getName().equals(role)) {
                oldRole = tmpRole;
                break;
            }
        }

        ArrayList<String> permissions = oldRole.getPermissions();
        ArrayList<String> newPermissions = new ArrayList<>();
        for (String perm : permissions) {
            if (!perm.equals(permission)) {
                newPermissions.add(perm);
            }
        }
        ArrayList<Role> newRoles = new ArrayList<>();
        for (Role tmpRole : roles) {
            if (!tmpRole.getName().equals(oldRole.getName())) {
                newRoles.add(tmpRole);
            }
        }
        oldRole.setPermissions(newPermissions);
        newRoles.add(oldRole);

        try (BufferedWriter br = new BufferedWriter(new FileWriter(path + "\\Roles.json"))) {
            br.write(gson.toJson(newRoles, type));
        } catch (IOException e) {
        }

        return true;
    }

    @Override
    public boolean deleteUserRole(String role) throws IOException {
        String path = storagePath;
        File file = new File(path + "\\Roles.json");
        if (!file.exists()) {
            file.createNewFile();
            return false;
        }

        Gson gson = new GsonBuilder().serializeNulls().create();
        ArrayList<Role> roles;
        Type type = new TypeToken<ArrayList<Role>>() {
        }.getType();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            roles = gson.fromJson(br, type);
        }

        if (roles == null) {
            return true;
        }

        ArrayList<Role> newRoles = new ArrayList<>();
        for (Role tmpRole : roles) {
            if (!tmpRole.getName().equals(role)) {
                newRoles.add(tmpRole);
            }
        }

        try (BufferedWriter br = new BufferedWriter(new FileWriter(path + "\\Roles.json"))) {
            br.write(gson.toJson(newRoles, type));
        } catch (IOException e) {
        }

        return true;
    }

    @Override
    public ArrayList<String> getPermissions() throws IOException {
        String path = storagePath;
        File file = new File(path + "\\Permissions.json");
        if (!file.exists()) {
            file.createNewFile();
            return new ArrayList<>();
        }

        ArrayList<String> permissions = new ArrayList<>();
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            permissions = gson.fromJson(br, type);
        }

        return permissions;
    }

    @Override
    public String createPermission(String permissionName) throws IOException {
        String path = storagePath;
        File file = new File(path + "\\Permissions.json");
        if (!file.exists()) {
            file.createNewFile();
        }

        ArrayList<String> permissions = getPermissions();

        if (permissions == null) {
            permissions = new ArrayList<>();
        }

        for (String tmpPermission : permissions) {
            if (tmpPermission.equals(permissionName)) {
                return permissionName;
            }
        }

        permissions.add(permissionName);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        Gson gson = new GsonBuilder().serializeNulls().create();
        try (BufferedWriter br = new BufferedWriter(new FileWriter(path + "\\Permissions.json"))) {
            br.write(gson.toJson(permissions, type));
        } catch (IOException e) {
        }

        return permissionName;
    }

    @Override
    public boolean deletePermission(String permissionName) throws IOException {
        String path = storagePath;
        File file = new File(path + "\\Permissions.json");
        if (!file.exists()) {
            file.createNewFile();
            return true;
        }

        ArrayList<String> permissions = getPermissions();
        ArrayList<String> newPermissions = new ArrayList<>();

        for (String tmpPermission : permissions) {
            if (!tmpPermission.equals(permissionName)) {
                newPermissions.add(tmpPermission);
            }
        }

        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        Gson gson = new GsonBuilder().serializeNulls().create();
        try (BufferedWriter br = new BufferedWriter(new FileWriter(path + "\\Permissions.json"))) {
            br.write(gson.toJson(newPermissions, type));
        }
        return true;
    }

    @Override
    public ArrayList<UserDataObject> getUsers() throws IOException {
        String filePath = storagePath + "\\Users\\Users.json";
        ArrayList<UserDataObject> users = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<UserDataObject>>() {
        }.getType();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            users = gson.fromJson(br, type);
        }

        return users;
    }

    @Override
    public UserDataObject getUser(String userId) throws IOException {
        ArrayList<UserDataObject> users = getUsers();
        if (users == null) {
            return null;
        }
        for (UserDataObject user : users) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public String createUser(UserDataObject user) throws IOException {
        ArrayList<UserDataObject> users = getUsers();
        if (users == null) {
            users = new ArrayList<>();
        }

        ArrayList<UserDataObject> newUsers = new ArrayList<>();
        for (UserDataObject tmpUser : users) {
            if (!tmpUser.getId().equals(user.getId())) {
                newUsers.add(tmpUser);
            } else {
                return null;
            }
        }

        newUsers.add(user);

        Type type = new TypeToken<ArrayList<UserDataObject>>() {
        }.getType();
        Gson gson = new GsonBuilder().serializeNulls().create();
        try (BufferedWriter br = new BufferedWriter(new FileWriter(storagePath + "\\Users\\Users.json"))) {
            br.write(gson.toJson(newUsers, type));
        } catch (IOException e) {
        }

        return user.getId();
    }

    @Override
    public boolean editUser(UserDataObject user) throws IOException {
        ArrayList<UserDataObject> users = getUsers();
        if (users == null) {
            users = new ArrayList<>();
        }

        ArrayList<UserDataObject> newUsers = new ArrayList<>();
        for (UserDataObject tmpUser : users) {
            if (!tmpUser.getId().equals(user.getId())) {
                newUsers.add(tmpUser);
            }
        }

        newUsers.add(user);

        Type type = new TypeToken<ArrayList<UserDataObject>>() {
        }.getType();
        Gson gson = new GsonBuilder().serializeNulls().create();
        try (BufferedWriter br = new BufferedWriter(new FileWriter(storagePath + "\\Users\\Users.json"))) {
            br.write(gson.toJson(newUsers, type));
        }

        return true;
    }

    @Override
    public boolean deleteUser(UserDataObject user) throws IOException {
        ArrayList<UserDataObject> users = getUsers();
        if (users == null) {
            return true;
        }

        ArrayList<UserDataObject> newUsers = new ArrayList<>();
        for (UserDataObject tmpUser : users) {
            if (!tmpUser.getId().equals(user.getId())) {
                newUsers.add(tmpUser);
            }
        }

        Type type = new TypeToken<ArrayList<UserDataObject>>() {
        }.getType();
        Gson gson = new GsonBuilder().serializeNulls().create();
        try (BufferedWriter br = new BufferedWriter(new FileWriter(storagePath + "\\Users\\Users.json"))) {
            br.write(gson.toJson(newUsers, type));
        } catch (IOException e) {
        }

        return true;
    }

    @Override
    public String getUserId(String login) throws IOException {
        ArrayList<UserDataObject> users = getUsers();
        if (users == null) {
            return null;
        }

        for (UserDataObject tmpUser : users) {
            if (tmpUser.getLogin().equals(login)) {
                return tmpUser.getId();
            }
        }

        return null;
    }

    private static void delete(File file)
            throws IOException {
        if (file.isDirectory()) {

            //directory is empty, then delete it
            if (file.list().length == 0) {
                file.delete();
                System.out.println("Directory is deleted : "
                        + file.getAbsolutePath());
            } else {
                //list all the directory contents
                String files[] = file.list();
                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);
                    //recursive delete
                    delete(fileDelete);
                }

                //check the directory again, if empty then delete it
                if (file.list().length == 0) {
                    file.delete();
                    System.out.println("Directory is deleted : "
                            + file.getAbsolutePath());
                }
            }
        } else {
            //if file, then delete it
            file.delete();
            System.out.println("File is deleted : " + file.getAbsolutePath());
        }
    }

    private TreeMap<String, TreeMap<Integer, AnnotationDataObject>> getDictionaryFromFile(String documentId) throws IOException {
        String path = storagePath + "Documents\\" + documentId;
        File file = new File(path + "\\" + "Annotations.json");
        if (!file.exists()) {
            file.createNewFile();
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<TreeMap<String, TreeMap<Integer, AnnotationDataObject>>>() {
        }.getType();
        TreeMap<String, TreeMap<Integer, AnnotationDataObject>> annotationsDictionary;
        try (BufferedReader br = new BufferedReader(new FileReader(path + "\\" + "Annotations.json"))) {
            annotationsDictionary = gson.fromJson(br, type);
        } catch (FileNotFoundException ex) {
            return null;
        }

        return annotationsDictionary;
    }
}
