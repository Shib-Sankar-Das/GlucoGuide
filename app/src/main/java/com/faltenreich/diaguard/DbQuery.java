package com.faltenreich.diaguard;

import android.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DbQuery {

    public static FirebaseFirestore g_firestore;
    public static List<TestModle> g_testList = new ArrayList<>();
    public static List<DoctorModle> g_doctorList = new ArrayList<>();

    public static void createUserData(String email, String name, MyCompleteListener myCompleteListener)
    {
        Map<String, Object> userData = new ArrayMap<>();

        userData.put("EMAIL", email);
        userData.put("NAME", name);

        DocumentReference userDoc = g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        WriteBatch batch = g_firestore.batch();

        batch.set(userDoc, userData);

        // Create DOCTOR collection with DOCTOR_LIST document
       /* DocumentReference doctorListDoc = userDoc.collection("DOCTOR").document("DOCTOR_LIST");
        Map<String, Object> doctorListData = new ArrayMap<>();
        doctorListData.put("COUNT", 0);
        batch.set(doctorListDoc, doctorListData);*/

        DocumentReference countDoc = g_firestore.collection("USERS").document("TOTAL_USERS");
        batch.update(countDoc, "COUNT", FieldValue.increment(1));

        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        myCompleteListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        myCompleteListener.onFailure();
                    }
                });
    }



    public static void loadUserData(MyCompleteListener myCompleteListener) {
        g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String email = documentSnapshot.getString("EMAIL");
                            String name = documentSnapshot.getString("NAME");

                            // You might want to store this data in a User object or somewhere accessible
                            // For now, let's assume you have a static User class to hold this info
                            User.setEmail(email);
                            User.setName(name);

                            Log.d("TAG", "DocumentSnapshot data: " + email);
                            Log.d("TAG", "DocumentSnapshot data: " + name);

                            myCompleteListener.onSuccess();
                        } else {
                            myCompleteListener.onFailure();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        myCompleteListener.onFailure();
                    }
                });
    }



    public static void loadTestData(MyCompleteListener myCompleteListener)
    {
        g_testList.clear();

        g_firestore.collection("LAB_TEST_DATA").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();

                for(QueryDocumentSnapshot doc : queryDocumentSnapshots)
                {
                    docList.put(doc.getId(),doc);
                }

                QueryDocumentSnapshot labListDoc = docList.get("Lab_List");

                long labCount = labListDoc.getLong("COUNT");

                for(int i = 1; i <= labCount; i++)
                {
                    String labID = labListDoc.getString("Lab_" + String.valueOf(i) + "_ID");

                    QueryDocumentSnapshot labData = docList.get(labID);

                    String labName = labData.getString("NAME");
                    String labAddress = labData.getString("ADDRESS");
                    String labContact = labData.getString("CONTACT");

                    g_testList.add(new TestModle(labID,labName,labAddress,labContact));

                }

                myCompleteListener.onSuccess();


            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        myCompleteListener.onFailure();

                    }
                });

    }


    public static void createDoctor(String name, String speciality, String address, String hospital, String email, String phone, boolean status, MyCompleteListener myCompleteListener)
    {
        Map<String, Object> doctorData = new ArrayMap<>();

        doctorData.put("DOC_NAME", name);
        doctorData.put("DOC_SPECIALITY", speciality);
        doctorData.put("DOC_ADDRESS", address);
        doctorData.put("DOC_HOSPITAL", hospital);
        doctorData.put("DOC_EMAIL", email);
        doctorData.put("DOC_PHONE", phone);
        doctorData.put("DOC_STATUS", status);

        DocumentReference doctorDoc = g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("DOCTOR").document(email);

        WriteBatch batch = g_firestore.batch();

        batch.set(doctorDoc, doctorData);

        /*DocumentReference doctorListDoc = g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("DOCTOR").document("DOCTOR_LIST");
        batch.update(doctorListDoc, "COUNT", FieldValue.increment(1));*/


        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                myCompleteListener.onSuccess();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        myCompleteListener.onFailure();
                    }
                });
    }


    public static void loadDoctorsData(MyCompleteListener myCompleteListener) {
        g_doctorList.clear();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        g_firestore.collection("USERS").document(userId).collection("DOCTOR").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();

                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            docList.put(doc.getId(), doc);
                        }

                        /*Log.d("TAG", "DocumentSnapshot data: " + docList.size());

                        Log.d("TAG", "DocumentSnapshot data: " + docList.get("hongjoochan88@gmail.com").getData());*/

                        Set<String> keys = docList.keySet();

                        for (String key : keys) {
                            //Log.d("TAG", "Document ID: " + key);

                                    if (key != null) {
                                        QueryDocumentSnapshot doctorData = docList.get(key);

                                        if (doctorData != null) {
                                            String doctorName = doctorData.getString("DOC_NAME");
                                            String doctorSpeciality = doctorData.getString("DOC_SPECIALITY");
                                            String doctorAddress = doctorData.getString("DOC_ADDRESS");
                                            String doctorHospital = doctorData.getString("DOC_HOSPITAL");
                                            String doctorEmail = doctorData.getString("DOC_EMAIL");
                                            String doctorPhone = doctorData.getString("DOC_PHONE");
                                            boolean doctorStatus = doctorData.getBoolean("DOC_STATUS");

                                            g_doctorList.add(new DoctorModle(doctorName, doctorSpeciality, doctorAddress, doctorHospital, doctorEmail, doctorPhone, doctorStatus));
                                        }

                                        else {
                                            Log.d("TAG", "DocumentSnapshot data: NULL");
                                        }
                                    }

                                    else {
                                        Log.d("TAG", "Document ID: NULL");
                                    }
                                }
                        //Log.d("TAG", "DocumentSnapshot data: " + g_doctorList.get(0));

                        myCompleteListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        myCompleteListener.onFailure();
                    }
                });
    }



        public static void updateDoctorStatus(String doctorEmail, boolean newStatus, MyCompleteListener myCompleteListener) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DocumentReference doctorRef = g_firestore.collection("USERS").document(userId)
                    .collection("DOCTOR").document(doctorEmail);

            doctorRef.update("DOC_STATUS", newStatus)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Update local data
                            for (DoctorModle doctor : g_doctorList) {
                                if (doctor.getDocEmail().equals(doctorEmail)) {
                                    doctor.setDocStatus(newStatus);
                                    break;
                                }
                            }
                            myCompleteListener.onSuccess();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            myCompleteListener.onFailure();
                        }
                    });
        }



        public static void updateDoctorData(String originalEmail, DoctorModle updatedDoctor, MyCompleteListener myCompleteListener) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DocumentReference doctorRef = g_firestore.collection("USERS").document(userId)
                    .collection("DOCTOR").document(originalEmail);

            Map<String, Object> updates = new HashMap<>();
            updates.put("DOC_NAME", updatedDoctor.getDoctorName());
            updates.put("DOC_SPECIALITY", updatedDoctor.getDocSpeciality());
            updates.put("DOC_ADDRESS", updatedDoctor.getDocAddress());
            updates.put("DOC_HOSPITAL", updatedDoctor.getDocHospital());
            updates.put("DOC_EMAIL", updatedDoctor.getDocEmail());
            updates.put("DOC_PHONE", updatedDoctor.getDocContact());

            doctorRef.update(updates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // If email has changed, we need to create a new document and delete the old one
                            if (!originalEmail.equals(updatedDoctor.getDocEmail())) {
                                DocumentReference newDoctorRef = g_firestore.collection("USERS").document(userId)
                                        .collection("DOCTOR").document(updatedDoctor.getDocEmail());

                                newDoctorRef.set(updates)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                doctorRef.delete()
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                updateLocalDoctorData(originalEmail, updatedDoctor);
                                                                myCompleteListener.onSuccess();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                myCompleteListener.onFailure();
                                                            }
                                                        });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                myCompleteListener.onFailure();
                                            }
                                        });
                            } else {
                                updateLocalDoctorData(originalEmail, updatedDoctor);
                                myCompleteListener.onSuccess();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            myCompleteListener.onFailure();
                        }
                    });
        }

        private static void updateLocalDoctorData(String originalEmail, DoctorModle updatedDoctor) {
            for (int i = 0; i < g_doctorList.size(); i++) {
                if (g_doctorList.get(i).getDocEmail().equals(originalEmail)) {
                    g_doctorList.set(i, updatedDoctor);
                    break;
                }
            }
        }

    public static List<DoctorModle> getUpdatedDoctorList() {
        return new ArrayList<>(g_doctorList);
    }


}
