from flask import Blueprint, Response, request, jsonify
from components.drivers.sensors.GPSSensor import GPSSensor

gps = None

gps_api = Blueprint('gps_api', __name__)


@gps_api.route("/gps", methods=['GET'])
def gps_value_request():
    gps_data = gps.get_data()
    if gps_data and gps_data.latitude:
        gps_json = {"sensor_name": "gps_sensor",
                    'latitude': gps_data.latitude,
                    'longitude': gps_data.longitude,
                    'altitude': gps_data.altitude,
                    'real': 1.0}
        return jsonify(gps_json)
    else:
        return Response(status=204)


@gps_api.route("/gps", methods=['POST'])
def gps_enable_request():
    enable = True if request.json['enabled'].lower() == "true" else False
    global gps
    gps = GPSSensor.get_instance()
    gps.enable_disable_driver(enable)
    return Response(request.data)
