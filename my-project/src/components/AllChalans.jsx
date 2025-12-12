import React, { useEffect, useState } from "react";
import API from "../api";
import jsPDF  from "jspdf";
import autoTable from "jspdf-autotable";

export default function AllChalans() {
  const [list, setList] = useState([]);

  useEffect(() => {
    load();
  }, []);

  const load = async () => {
    try {
      const res = await API.get("/chalans");
      setList(res.data);
    } catch (err) {
      console.error("Error loading chalans:", err);
    }
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
    <div className="max-w-7xl mx-auto p-4">
      <h2 className="text-2xl font-bold mb-6 text-gray-800 text-center">
        All Chalans
      </h2>

      <div className="bg-white shadow-md rounded-lg overflow-x-auto">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Chalan No
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Date
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Original Meter
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Updated Meter
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Mill Name
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Color Code
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Border Shop
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
                  {c.date}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-gray-600">
                  {c.meter}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-gray-600">
                  {c.updatedMeter || c.meter || "-"}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-gray-600">
                  {c.millName || "-"}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-gray-600">
                  {c.colorCode || "-"}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-gray-600">
                  {c.shopName || "-"}
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
            No chalans available.
          </p>
        )}
      </div>
    </div>
  );
}
