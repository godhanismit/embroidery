import React, { useState } from "react";
import API from "../api";

export default function CreateChalan() {
  const [form, setForm] = useState({
    chalanNo: "",
    date: new Date().toISOString().split("T")[0],
    rate: "",
    meter: "",
  });

  const onChange = (e) =>
    setForm({ ...form, [e.target.name]: e.target.value });

  const create = async (e) => {
    e.preventDefault();
    try {
      await API.post("/chalans", {
        chalanNo: form.chalanNo,
        date: form.date,
        rate: parseFloat(form.rate),
        meter: parseFloat(form.meter),
      });

      alert("Chalan created!");
      setForm({ chalanNo: "", date: "", rate: "", meter: "" });
    } catch {
      alert("Error creating chalan");
    }
  };

  return (
    <div className="max-w-md mx-auto mt-6 bg-white shadow-lg rounded-lg p-6">
      <h2 className="text-2xl font-bold mb-6 text-gray-800 text-center">
        Create Chalan
      </h2>

      <form onSubmit={create} className="space-y-4">
        <div>
          <label className="block text-gray-700 mb-1">Chalan No</label>
          <input
            name="chalanNo"
            placeholder="Enter Chalan No"
            value={form.chalanNo}
            onChange={onChange}
            className="w-full p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
            required
          />
        </div>

        <div>
          <label className="block text-gray-700 mb-1">Date</label>
          <input
            name="date"
            type="date"
            value={form.date}
            onChange={onChange}
            className="w-full p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
            required
          />
        </div>

        <div>
          <label className="block text-gray-700 mb-1">Rate</label>
          <input
            name="rate"
            placeholder="Enter Rate"
            value={form.rate}
            onChange={onChange}
            className="w-full p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
            required
          />
        </div>

        <div>
          <label className="block text-gray-700 mb-1">Meter</label>
          <input
            name="meter"
            placeholder="Enter Meter"
            value={form.meter}
            onChange={onChange}
            className="w-full p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
            required
          />
        </div>

        <button
          type="submit"
          className="w-full bg-blue-600 hover:bg-blue-700 text-white font-semibold px-4 py-3 rounded-lg transition-all"
        >
          Create Chalan
        </button>
      </form>
    </div>
  );
}
