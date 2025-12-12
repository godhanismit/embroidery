import React, { useEffect, useState } from "react";
import API from "../api";

export default function MillSection() {
  const [list, setList] = useState([]);
  const [selected, setSelected] = useState(null);
  const [dto, setDto] = useState({
    updatedMeter: "",
    millName: "",
    colorCode: "",
  });

  const load = async () => {
    try {
      const res = await API.get("/chalans/status/CREATED");
      setList(res.data);
    } catch (err) {
      console.error("Error loading chalans:", err);
    }
  };

  useEffect(() => {
    load();
  }, []);

  const send = async () => {
    if (!dto.updatedMeter || !dto.millName || !dto.colorCode) {
      alert("Please fill all fields!");
      return;
    }

    try {
      await API.post(`/chalans/${selected.id}/to-mill`, {
        updatedMeter: parseFloat(dto.updatedMeter),
        millName: dto.millName,
        colorCode: dto.colorCode,
      });

      alert("Transferred to Mill");
      setSelected(null);
      setDto({ updatedMeter: "", millName: "", colorCode: "" });
      load();
    } catch (err) {
      console.error("Error sending to mill:", err);
      alert("Failed to transfer chalan");
    }
  };

  return (
    <div className="max-w-6xl mx-auto p-4">
      <h2 className="text-2xl font-bold mb-6 text-gray-800 text-center">
        Mill Section
      </h2>

      {!selected ? (
        <div className="bg-white shadow-md rounded-lg overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Chalan No
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Meter
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Actions
                </th>
              </tr>
            </thead>

            <tbody className="bg-white divide-y divide-gray-200">
              {list.map((c) => (
                <tr key={c.id} className="hover:bg-gray-50">
                  <td className="px-6 py-4 whitespace-nowrap text-gray-800 font-medium">
                    {c.chalanNo}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-gray-600">
                    {c.meter}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <button
                      className="text-blue-600 font-medium hover:underline"
                      onClick={() => {
                        setSelected(c);
                        setDto({
                          updatedMeter: c.updatedMeter || c.meter,
                          millName: "",
                          colorCode: "",
                        });
                      }}
                    >
                      Process
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          {list.length === 0 && (
            <p className="text-center text-gray-400 py-6">
              No chalans available for processing.
            </p>
          )}
        </div>
      ) : (
        <div className="bg-white shadow-md rounded-lg p-6 max-w-md mx-auto">
          <h3 className="text-xl font-semibold mb-4 text-gray-800 text-center">
            Process Chalan #{selected.chalanNo}
          </h3>

          <input
            className="w-full p-3 border rounded-lg mb-4 focus:outline-none focus:ring-2 focus:ring-blue-400"
            placeholder="Updated Meter"
            value={dto.updatedMeter}
            onChange={(e) =>
              setDto({ ...dto, updatedMeter: e.target.value })
            }
          />

          <input
            className="w-full p-3 border rounded-lg mb-4 focus:outline-none focus:ring-2 focus:ring-blue-400"
            placeholder="Mill Name"
            value={dto.millName}
            onChange={(e) => setDto({ ...dto, millName: e.target.value })}
          />

          <input
            className="w-full p-3 border rounded-lg mb-4 focus:outline-none focus:ring-2 focus:ring-blue-400"
            placeholder="Color Code"
            value={dto.colorCode}
            onChange={(e) => setDto({ ...dto, colorCode: e.target.value })}
          />

          <div className="flex justify-center">
            <button
              onClick={send}
              className="bg-blue-600 hover:bg-blue-700 text-white font-semibold px-6 py-3 rounded-lg transition-all"
            >
              Submit
            </button>

            <button
              onClick={() => {
                setSelected(null);
                setDto({ updatedMeter: "", millName: "", colorCode: "" });
              }}
              className="ml-3 bg-gray-300 hover:bg-gray-400 text-gray-800 font-semibold px-6 py-3 rounded-lg transition-all"
            >
              Cancel
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
