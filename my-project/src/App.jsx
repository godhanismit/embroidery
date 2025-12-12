import React from "react";
import { BrowserRouter, Routes, Route, NavLink } from "react-router-dom";
import CreateChalan from "./components/CreateChalan";
import MillSection from "./components/MillSection";
import BorderSection from "./components/BorderSection";
import MainShopSection from "./components/MainShopSection";
import AllChalans from "./components/AllChalans";
import FilterChalans from "./components/FilterChalans";

export default function App() {
  const navItems = [
    { name: "All Chalans", path: "/" },
    { name: "Create Chalan", path: "/create" },
    { name: "Mill Section", path: "/mill" },
    { name: "Border Cutting", path: "/border" },
    { name: "Main Shop", path: "/main" },
    { name: "Filter Chalans", path: "/filter" },
  ];

  return (
    <BrowserRouter>
      <div className="min-h-screen bg-gray-100 p-6">
        {/* Navbar */}
        <nav className="bg-white shadow-md rounded-lg mb-6 p-4 flex flex-wrap gap-3 justify-center">
          {navItems.map((item) => (
            <NavLink
              key={item.path}
              to={item.path}
              className={({ isActive }) =>
                `px-4 py-2 rounded-lg font-medium transition-colors duration-200 ${
                  isActive
                    ? "bg-blue-600 text-white shadow-md"
                    : "text-blue-600 hover:bg-blue-50"
                }`
              }
            >
              {item.name}
            </NavLink>
          ))}
        </nav>

        {/* Routes */}
        <Routes>
          <Route path="/" element={<AllChalans />} />
          <Route path="/create" element={<CreateChalan />} />
          <Route path="/mill" element={<MillSection />} />
          <Route path="/border" element={<BorderSection />} />
          <Route path="/main" element={<MainShopSection />} />
          <Route path="/filter" element={<FilterChalans />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}
