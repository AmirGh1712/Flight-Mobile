using System;
using System.Text.Json.Serialization;
using Newtonsoft.Json;

namespace FlightMobileWeb.Model
{
    public class Command
    {
        [JsonPropertyName("aileron")]
        [JsonProperty("aileron")]
        public double Aileron { get; set; }

        [JsonPropertyName("rudder")]
        [JsonProperty("rudder")]
        public double Rudder { get; set; }

        [JsonPropertyName("elevator")]
        [JsonProperty("elevator")]
        public double Elevator { get; set; }

        [JsonPropertyName("Throttle")]
        [JsonProperty("Throttle")]
        public double Throttle { get; set; }


        public Command()
        {
        }

        public Command(double aileron, double rudder, double elevator, double throttle)
        {
            Aileron = aileron;
            Rudder = rudder;
            Elevator = elevator;
            Throttle = throttle;
        }

    }
}
