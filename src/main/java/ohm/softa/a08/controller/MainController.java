package ohm.softa.a08.controller;

import com.google.gson.Gson;
import ohm.softa.a08.Filter.IMealsFilter;
import ohm.softa.a08.Filter.MealsFilterFactory;
import ohm.softa.a08.Service.OpenMensaAPIService;
import ohm.softa.a08.api.OpenMensaAPI;
import ohm.softa.a08.model.Meal;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller for main.fxml
 *
 * @author Peter Kurfer
 */
public class MainController implements Initializable {

	/**
	 * Logger e.g. to debug multi-threading issues
	 */
	private static final Logger logger;

	/**
	 * DateFormat instance to generate required date format string for OpenMensa API
	 */
	private static final DateFormat openMensaDateFormat;

	private final OpenMensaAPI api;
	private final ObservableList<Meal> meals;
	private final LinkedList<Meal> unfilteredMeals;
	private final Gson gson;

	/**
	 * Binding of ChoiceBox UI element to filter for certain types of meals
	 */
	@FXML
	private ChoiceBox<String> filterChoiceBox;

	/**
	 * Binding of ListView UI element to display meals
	 */
	@FXML
	private ListView<Meal> mealsListView;

	/*
	  static initializer to initialize fields in class
	 */
	static {
		logger = LogManager.getLogger(MainController.class);
		openMensaDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
	}

	/**
	 * Default constructor
	 */
	public MainController() {
		meals = FXCollections.observableArrayList();
		unfilteredMeals = new LinkedList<>();
		gson = new Gson();

		OpenMensaAPIService apiService = OpenMensaAPIService.getInstance();

		api = apiService.getAPI();
	}

	/**
	 * Initialization method of the UI controller
	 * Called after the FXML fields are assigned
	 *
	 * @param location
	 * @param resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mealsListView.setItems(meals);
		filterChoiceBox.setItems(FXCollections.observableList(Arrays.asList(gson.fromJson(new InputStreamReader(getClass().getResourceAsStream("/filters.json")), String[].class))));
		doGetMeals();
	}

	/**
	 * Handles fetching of meals from OpenMensa API
	 */
	@FXML
	public void doGetMeals() {
		api.getMeals(openMensaDateFormat.format(new Date())).enqueue(new Callback<>() {

			@Override
			public void onResponse(Call<List<Meal>> call, Response<List<Meal>> response) {
				logger.debug("Got response");

				Platform.runLater(() -> {
					if (!response.isSuccessful() || response.body() == null) {
						logger.error(String.format("Got response with not successfull code %d", response.code()));

							var alert = new Alert(Alert.AlertType.ERROR);
							alert.setHeaderText("Unsuccessful HTTP call");
							alert.setContentText("Failed to get meals from OpenMensaAPI");
							alert.show();

						return;
					}
					unfilteredMeals.clear();
					unfilteredMeals.addAll(response.body());
					applyMealFilter();
				});
			}

			@Override
			public void onFailure(Call<List<Meal>> call, Throwable t) {
				logger.error("Failed to fetch meals");
				Platform.runLater(() -> {
					var alert = new Alert(Alert.AlertType.ERROR);
					alert.setHeaderText("Failed HTTP call");
					alert.setContentText("Failed to submit HTTP call to fetch meals.");
					alert.show();
				});
			}
		});
	}

	@FXML
	public void applyMealFilter(){
		IMealsFilter filter = MealsFilterFactory.getStrategy(filterChoiceBox.getValue());
		meals.clear();
		meals.addAll(filter.filter(unfilteredMeals));
	}

}
