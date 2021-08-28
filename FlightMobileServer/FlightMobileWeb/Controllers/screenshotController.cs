using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;

namespace FlightMobileWeb.Controllers
{
    [Route("screenshot")]
    [ApiController]
    public class screenshotController : ControllerBase
    {
        private HttpClient client;
        private string _url;

        public screenshotController(IConfiguration configuration)
        {
            client = new HttpClient();
            _url = configuration["SimHttp"];
        }


        // GET: /screenshot
        [Route("")]
        public async Task<ActionResult> Get()
        {
            HttpClient client = new HttpClient();

            //string ip="127.0.0.1", httpPort="5000";

            //string url = "http://" + ip + ":" + httpPort + "/screenshot";


            var result = await client.GetAsync(_url);

            byte[] content = await result.Content.ReadAsByteArrayAsync();

            if (content != null)
            {
                return File(content, "image/jpeg");
            }
                
            return BadRequest();
        }

        
    }
}
