package ro.planner.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ro.planner.clientapp.R;
import ro.planner.adapter.HistoryAdapter;
import ro.planner.model.HistoryModel;

public class HistoryFragment extends Fragment {
    private String username;
    private HistoryAdapter adapter;

    ArrayList<HistoryModel> historyModels = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //remove the status bar
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_history, container, false);

        Bundle args = getArguments();
        if (args != null) {
            username = args.getString("username");
        }

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewHistory);

        historyModels.clear();
        return  view;
    }
}
        //get all the location
//        getLocations(new LocationAPI.LocationListCallback() {
//
//            @Override
//            public void onSuccess(List<Location> locationList) {
//                boolean foundValidLocation = false; //pt a verifica ca nu s-a gasit o locatie valida si copilul are gps-ul oprit
//
//                for(int i = 0; i<locationList.size();i++){
//                    double latitude = locationList.get(i).getLatitude();
//                    double longitude = locationList.get(i).getLongitude();
//
//                    // If the GPS is closed
//                    if (Double.isNaN(latitude) || Double.isNaN(longitude)) {
//                        continue; // move on
//                    }
//                    //create the address to show based on latitude and longitude
//                    String address = getAddress(latitude, longitude);
//
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
//
//                    //get arrival time
//                    String arrTime = locationList.get(i).getArrivalTime().toString();
//                    LocalDateTime arrDateTime = LocalDateTime.parse(arrTime);
//                    String formattedArrDate = "Arrival time:" + arrDateTime.format(formatter);
//
//                    //get departured time
//                    String deptTime = locationList.get(i).getDepartureTime().toString();
//                    LocalDateTime deptDateTime = LocalDateTime.parse(deptTime);
//                    String formattedDeptDate =  "Departure time:" + deptDateTime.format(formatter);
//
//                    System.out.println(">>>>>>>>>>>>>> HistoryFragment" + latitude + " " + longitude);
//                    //add a new location to the model
//                    historyModels.add(new HistoryModel(
//                            address,
//                            formattedArrDate,
//                            formattedDeptDate,
//                            locationList.get(i).getEmail(),
//                            longitude,
//                            latitude
//                    ));
//                    foundValidLocation = true;
//                }
//                adapter.notifyDataSetChanged();
//                if (!foundValidLocation) {
//                    Toast.makeText(getContext(), "No location found yet!", Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onError(String message) {
//                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
//            }
//        });
//
//        adapter = new HistoryAdapter(getContext(), historyModels);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        return view;
//    }
//
//
//    public void getLocations(final LocationAPI.LocationListCallback callback) {
//        locationAPI.getHistoryLocations(getContext(), username, token, new LocationAPI.LocationListCallback() {
//            @Override
//            public void onSuccess(List<Location> locationList) {
//                callback.onSuccess(locationList);
//            }
//
//            @Override
//            public void onError(String message) {
//                callback.onError(message);
//            }
//        });
//    }
//
//
//
//    private String getAddress(double latitude, double longitude) {
//        //create the address based on latutude and longitude
//        String address = "";
//
//        try {
//            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
//            if (Double.isNaN(latitude) || Double.isNaN(longitude)) {
//                //ignore values
//            }
//            else{
//                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
//
//                if (addresses != null && addresses.size() > 0) {
//                    Address returnedAddress = addresses.get(0);
//                    StringBuilder sb = new StringBuilder();
//
//                    for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
//                        sb.append(returnedAddress.getAddressLine(i)).append("\n");
//                    }
//
//                    address = sb.toString();
//                }
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return address;
//    }
//

//}
