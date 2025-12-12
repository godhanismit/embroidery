import React, { useEffect, useState } from "react";
import API from "../api";

export default function MainShopSection() {
  const [list, setList] = useState([]);

  useEffect(() => {
    load();
  }, []);

  const load = async () => {
    const res = await API.get("/chalans/status/AT_BORDER_CUTTING");
    setList(res.data);
  };

  const bringBack = async (id) => {
    await API.post(`/chalans/${id}/return`);
    alert("Moved to main shop");
    load();
  };

  return (
    <div className="max-w-6xl mx-auto p-4">
      <h2 className="text-2xl font-bold mb-6 text-gray-800 text-center">
        Main Shop Section
      </h2>

      <div className="bg-white shadow-md rounded-lg overflow-x-auto">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Chalan No
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Shop Name
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
                  {c.shopName}
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <button
                    onClick={() => bringBack(c.id)}
                    className="text-blue-600 font-medium hover:underline"
                  >
                    Receive Back
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>

        {list.length === 0 && (
          <p className="text-center text-gray-400 py-6">
            No chalans available at border cutting.
          </p>
        )}
      </div>
    </div>
  );
}
