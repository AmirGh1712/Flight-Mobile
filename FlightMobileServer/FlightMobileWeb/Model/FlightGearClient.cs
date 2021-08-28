using System;
using System.Collections.Concurrent;
using System.IO;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace FlightMobileWeb.Model
{
    public class FlightGearClient : IFlightGearClient
    {
        private readonly BlockingCollection<AsyncCommand> _queue;
        private readonly TcpClient _client;
        NetworkStream _stream;


        public FlightGearClient(TcpClient client)
        {
            _queue = new BlockingCollection<AsyncCommand>();
            _client = client;
            _stream = _client.GetStream();
            string firstMsg = "data\n";
            byte[] message = Encoding.ASCII.GetBytes(firstMsg);
            _stream.Write(message, 0, message.Length);
            Start();
        }



        // Called by the WebApi Controller, it will await on the returned Task<>
        // This is not an async method, since it does not await anything.
        public Task<Result> Execute(Command cmd)
        {
            var asyncCommand = new AsyncCommand(cmd);
            _queue.Add(asyncCommand);
            return asyncCommand.Task;
        }



        public void Start()
        {
            Task.Factory.StartNew(ProcessCommands);
        }



        public void ProcessCommands()
        {
            //_client.Connect("127.0.0.1", 5403);
            //string firstMsg = "data\n";
            //byte[] message = Encoding.ASCII.GetBytes(firstMsg);
            //stream.Write(message, 0, message.Length);

            //NetworkStream stream = _client.GetStream();
            foreach (AsyncCommand command in _queue.GetConsumingEnumerable())
            {
                bool ok1, ok2, ok3, ok4 = true;

                byte[] sendBuffer = Encoding.ASCII.GetBytes("set " + "/controls/flight/aileron " + command.Command.Aileron + "\r\n");
                //byte[] recvBuffer = new byte[1024];
                _stream.Write(sendBuffer, 0, sendBuffer.Length);
                //int nRead = stream.Read(recvBuffer, 0, 1024);



                StreamReader reader = new StreamReader(_stream);

                ok1 = ReadOneLine(reader, command.Command.Aileron);

                sendBuffer = Encoding.ASCII.GetBytes("set " + "/controls/engines/current-engine/throttle " + command.Command.Throttle + "\r\n");
                //byte[] recvBuffer = new byte[1024];
                _stream.Write(sendBuffer, 0, sendBuffer.Length);

                ok2 = ReadOneLine(reader, command.Command.Throttle);

                sendBuffer = Encoding.ASCII.GetBytes("set " + "/controls/flight/rudder " + command.Command.Rudder + "\r\n");
                //byte[] recvBuffer = new byte[1024];
                _stream.Write(sendBuffer, 0, sendBuffer.Length);

                ok3 = ReadOneLine(reader, command.Command.Rudder);

                sendBuffer = Encoding.ASCII.GetBytes("set " + "/controls/flight/elevator " + command.Command.Elevator + "\r\n");
                //byte[] recvBuffer = new byte[1024];
                _stream.Write(sendBuffer, 0, sendBuffer.Length);

                ok4 = ReadOneLine(reader, command.Command.Elevator);

                Result res;
                if (ok1 && ok2 && ok3 && ok4)
                {
                    res = Result.Ok;
                }
                else
                {
                    res = Result.NotOk;
                }

                command.Completion.SetResult(res);
            }
        }



        public byte[] setMessage(Command command)
        {
            string str = "set " + "/controls/flight/aileron " + command.Aileron + "\r\n";
            str += "set " + "/controls/engines/current-engine/throttle " + command.Throttle + "\r\n";
            str += "set " + "/controls/flight/rudder " + command.Rudder + "\r\n";
            str += "set " + "/controls/flight/elevator " + command.Elevator + "\r\n";

            byte[] message = Encoding.ASCII.GetBytes(str);
            return message;
        }



        private bool ReadOneLine(StreamReader streamReader, double value)
        {
            string readMessage;
            try
            {
                return true;
                readMessage = streamReader.ReadLine();
                Console.WriteLine(readMessage);

                if (!Double.TryParse(readMessage, out double result))
                {
                    return false;
                }
                else if (value == result)
                {
                    return true;
                }

                return false;
            }
            
            catch (Exception)
            {
                return false;
            }
        }
    }
}
