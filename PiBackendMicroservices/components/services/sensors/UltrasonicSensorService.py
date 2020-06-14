from components.drivers.sensors.UltrasonicSensor import UltrasonicSensor
from components.services.AbstractComponentService import AbstractComponentService
import rpyc


class UltrasonicSensorService(rpyc.Service, AbstractComponentService):

    ALIASES = ["UltrasonicSensor"]

    exposed_DISTANCE_SAMPLING_FREQ = UltrasonicSensor.get_instance().DISTANCE_SAMPLING_FREQ

    def on_connect(self, conn):
        self.sensor = UltrasonicSensor.get_instance()

    def on_disconnect(self, conn):
        self.exposed_stop()

    def exposed_start(self):
        self.sensor.enable_disable_driver(True)
        self.started = True

    def exposed_stop(self):
        self.sensor.enable_disable_driver(False)
        self.started = False

    def exposed_get_data(self):
        if not self.started:
            self.sensor.enable_disable_driver(True)
        return self.sensor.get_data()

    def exposed_get_filtered_data(self):
        if not self.started:
            self.sensor.enable_disable_driver(True)
        return self.sensor.get_filtered_data()


if __name__ == "__main__":
    from rpyc.utils.server import ThreadedServer

    server = ThreadedServer(UltrasonicSensorService(), port=11121, auto_register=True)
    server.start()
