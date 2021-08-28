using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using FlightMobileWeb.Model;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;

namespace FlightMobileWeb.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class commandController : ControllerBase
    {

        private IFlightGearClient flightGearClient;
        private IConfiguration configuration;

        public commandController(IFlightGearClient _flightGearClient, IConfiguration _configuration)
        {
            flightGearClient = _flightGearClient;
            configuration = _configuration;
        }

        // POST: api/command
        [HttpPost]
        public async Task<ActionResult> Post([FromBody] Command command)
        {
            Result res = await flightGearClient.Execute(command);
            if (res == Result.NotOk)
            {
                return BadRequest();
            }
            else
            {
                return Ok();
            }
        }


    }
}
