import React, { useEffect, useState } from "react";
import API from "../api";
import jsPDF  from "jspdf";
import autoTable from "jspdf-autotable";

export default function FilterChalan() {
  const [list, setList] = useState([]);
  const [filters, setFilters] = useState({
    colorCode: "",
    shopName: "",
    millName: "",
    status: "", // added status filter
  });

  useEffect(() => {
    load();
  }, [filters]);

  const load = async () => {
    const res = await API.post("/chalans/filter", filters);
    setList(res.data);
  };

  const handleChange = (e) => {
    setFilters({ ...filters, [e.target.name]: e.target.value });
  };

  const applyFilter = () => {
    load();
  };

  const resetFilter = () => {
    setFilters({ colorCode: "", shopName: "", millName: "", status: "" });
    load();
  };

  const downloadPdf = (chalan) => {
      const doc = new jsPDF();
  
      // Title
      doc.setFontSize(18);
      doc.text("Chalan", 14, 20);
  
      // Basic Info
      doc.setFontSize(12);
      doc.text(`Chalan No: ${chalan.chalanNo}`, 14, 30);
      doc.text(`Date: ${chalan.date || "-"}`, 14, 37);
      doc.text(`Status: ${chalan.status.replaceAll("_", " ")}`, 14, 44);
  
      // Table Data
      autoTable(doc, { // <-- Use autoTable function directly
        startY: 55,
        head: [["Field", "Value"]],
        body: [
          ["Original Meter", chalan.meter || "-"],
          ["Updated Meter", chalan.updatedMeter || chalan.meter || "-"],
          ["Color Code", chalan.colorCode || "-"],
          ["Mill Name", chalan.millName || "-"],
          ["Border Shop", chalan.shopName || "-"],
        ],
        theme: "grid",
        headStyles: { fillColor: [30, 144, 255], textColor: 255 },
        styles: { cellPadding: 3, fontSize: 11 },
      });
  
      doc.save(`chalan-${chalan.chalanNo}.pdf`);
    };

  return (
    <div className="max-w-6xl mx-auto p-4">
      <h2 className="text-2xl font-bold mb-6 text-gray-800 text-center">
        Filter Chalans
      </h2>

      {/* Filters */}
      <div className="bg-white shadow-md rounded-lg p-6 mb-6">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <input
            className="p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
            placeholder="Filter by Color"
            name="colorCode"
            value={filters.colorCode}
            onChange={handleChange}
          />

          <input
            className="p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
            placeholder="Filter by Shop Name"
            name="shopName"
            value={filters.shopName}
            onChange={handleChange}
          />

          <input
            className="p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
            placeholder="Filter by Mill Name"
            name="millName"
            value={filters.millName}
            onChange={handleChange}
          />

          {/* Status dropdown */}
          <select
            name="status"
            value={filters.status}
            onChange={handleChange}
            className="p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
          >
            <option value="">All Status</option>
            <option value="CREATED">CREATED</option>
            <option value="AT_MILL">AT_MILL</option>
            <option value="AT_BORDER_CUTTING">AT_BORDER_CUTTING</option>
            <option value="COMPLETED">COMPLETED</option>
          </select>
        </div>

        <div className="mt-4 flex flex-wrap gap-3 justify-center md:justify-start">
          <button
            onClick={applyFilter}
            className="bg-blue-600 hover:bg-blue-700 text-white font-semibold px-6 py-3 rounded-lg transition-all"
          >
            Apply Filter
          </button>

          <button
            onClick={resetFilter}
            className="bg-gray-300 hover:bg-gray-400 text-gray-800 font-semibold px-6 py-3 rounded-lg transition-all"
          >
            Reset
          </button>
        </div>
      </div>

      {/* Chalan List */}
      <div className="bg-white shadow-md rounded-lg overflow-x-auto">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Chalan No
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Updated Meter
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Color Code
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Border Shop
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Mill Name
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Status
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
                  {c.updatedMeter || c.meter}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-gray-600">
                  {c.colorCode || "-"}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-gray-600">
                  {c.shopName || "-"}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-gray-600">
                  {c.millName || "-"}
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <span
                    className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                      c.status === "CREATED"
                        ? "bg-blue-100 text-blue-800"
                        : c.status === "AT_MILL"
                        ? "bg-yellow-100 text-yellow-800"
                        : c.status === "AT_BORDER_CUTTING"
                        ? "bg-purple-100 text-purple-800"
                        : "bg-green-100 text-green-800"
                    }`}
                  >
                    {c.status.replaceAll("_", " ")}
                  </span>
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <button
                    onClick={() => downloadPdf(c)}
                    className="text-blue-600 font-medium hover:underline"
                  >
                    Download PDF
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>

        {list.length === 0 && (
          <p className="text-center text-gray-400 py-6">
            No chalans found for selected filters.
          </p>
        )}
      </div>
    </div>
  );
}
