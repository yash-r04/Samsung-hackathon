from flask import Flask, render_template, request, jsonify
import requests
import folium
from shapely.geometry import LineString, Polygon, MultiPolygon, mapping
import openrouteservice
import osmnx as ox

app = Flask(__name__)
ORS_API_KEY = '5b3ce3597851110001cf6248f6f51f2475c7467eb55d9888559fcdfd'  # Replace with your new API key
ors = openrouteservice.Client(key=ORS_API_KEY)

# GeoJSON style function
def style_function(color):
    return lambda feature: dict(color=color, weight=3, opacity=0.5)

@app.route('/', methods=['GET', 'POST'])
def index():
    if request.method == 'POST':
        # Get dropped location from form submission
        try:
            dropped_lat = float(request.form['latitude'])
            dropped_lon = float(request.form['longitude'])
        except ValueError:
            return "Invalid coordinates"

        # Initialize Folium map centered at the dropped location
        my_map = folium.Map(location=[dropped_lat, dropped_lon], zoom_start=12)

        # Add custom marker for dropped location
        folium.Marker(
            location=[dropped_lat, dropped_lon],
            popup='Dropped Location',
            icon=folium.Icon(color='blue')
        ).add_to(my_map)

        # Example coordinates for start and end points (replace with actual data)
        start_coords = [dropped_lon + 0.02, dropped_lat - 0.01]
        end_coords = [dropped_lon - 0.02, dropped_lat + 0.01]
        
        # Treat the dropped location as a construction site polygon (a small buffer around the dropped location)
        site_poly = Polygon([
            (dropped_lon - 0.001, dropped_lat - 0.001),
            (dropped_lon + 0.001, dropped_lat - 0.001),
            (dropped_lon + 0.001, dropped_lat + 0.001),
            (dropped_lon - 0.001, dropped_lat + 0.001),
            (dropped_lon - 0.001, dropped_lat - 0.001)
        ])

        # Request normal route between start and end coordinates without construction sites
        request_params = {
            'coordinates': [start_coords, end_coords],
            'format_out': 'geojson',
            'profile': 'driving-car',
            'preference': 'shortest',
            'instructions': False,
            'radiuses': [500, 500]  # Increase search radius to 500 meters
        }

        try:
            # Fetch route data from OpenRouteService
            route_normal = ors.directions(**request_params)
        except openrouteservice.exceptions.ApiError as e:
            return f"API Error: {e}"

        # Add route without construction sites to the map
        folium.features.GeoJson(
            data=route_normal,
            name='Route without construction sites',
            style_function=style_function('#FF0000'),
            overlay=True
        ).add_to(my_map)

        # Buffer route with 0.009 degrees (for demonstration)
        route_buffer = LineString(route_normal['features'][0]['geometry']['coordinates']).buffer(0.009)

        # Add route buffer to the map
        folium.features.GeoJson(
            data=mapping(route_buffer),
            name='Route Buffer',
            style_function=style_function('#FFFF00'),
            overlay=True
        ).add_to(my_map)

        # Plot construction site (dropped location) if it falls into the buffer
        if route_buffer.intersects(site_poly):
            folium.Marker(
                location=[dropped_lat, dropped_lon],
                popup='Construction Site',
                icon=folium.Icon(color='red', icon='wrench')
            ).add_to(my_map)

        # Add the site polygon to the request parameters
        request_params['options'] = {'avoid_polygons': mapping(site_poly)}

        try:
            # Fetch detour route data from OpenRouteService
            route_detour = ors.directions(**request_params)
        except openrouteservice.exceptions.ApiError as e:
            return f"API Error: {e}"

        # Add route with construction sites to the map
        folium.features.GeoJson(
            data=route_detour,
            name='Route with construction sites',
            style_function=style_function('#00FF00'),
            overlay=True
        ).add_to(my_map)

        # Add layer control to toggle routes
        my_map.add_child(folium.map.LayerControl())

        # Render the map
        return my_map._repr_html_()

    # Render initial form
    return render_template('index.html')

def get_coordinates(location):
    try:
        gdf = ox.geocode_to_gdf(location)
        if gdf.empty:
            raise ValueError(f"Geocoding failed for location: {location}")
        # Ensure the geometry is valid and get the coordinates
        geometry = gdf.iloc[0].geometry
        if geometry.is_empty or geometry.geom_type != 'Point':
            raise ValueError(f"Invalid geometry for location: {location}")
        return geometry.coords[0]
    except Exception as e:
        raise ValueError(f"Error obtaining coordinates for location: {location}, {str(e)}")

@app.route('/route')
def show_route():
    start_location = request.args.get('start')
    end_location = request.args.get('end')

    try:
        start_coords = get_coordinates(start_location)
        end_coords = get_coordinates(end_location)
    except ValueError as e:
        return jsonify({'error': str(e)}), 500

    try:
        route = ors.directions(
            coordinates=[start_coords, end_coords],
            profile='driving-car',
            format='geojson'
        )
    except requests.exceptions.HTTPError as e:
        return jsonify({'error': 'Routing service error: ' + str(e)}), 500

    return jsonify(route)

if __name__ == '__main__':
    app.run(debug=True)
