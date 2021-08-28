using System;
using System.Threading.Tasks;

namespace FlightMobileWeb.Model
{
    public interface IFlightGearClient
    {
        public Task<Result> Execute(Command cmd);
        public void Start();
        public void ProcessCommands();
    }
}
