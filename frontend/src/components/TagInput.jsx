import { useState } from "react";

export default function TagInput({ value, onChange }) {
  const [input, setInput] = useState("");

  function addTag() {
    const t = input.trim();
    if (!t) return;
    if (!value.includes(t)) onChange([...value, t]);
    setInput("");
  }

  function removeTag(t) {
    onChange(value.filter((x) => x !== t));
  }

  return (
    <div>
      <div style={{ display: "flex", gap: 8, flexWrap: "wrap", marginBottom: 8 }}>
        {value.map((t) => (
          <span key={t} style={{ border: "1px solid #ccc", padding: "2px 8px", borderRadius: 12 }}>
            {t}{" "}
            <button type="button" onClick={() => removeTag(t)} style={{ marginLeft: 6 }}>
              ×
            </button>
          </span>
        ))}
      </div>
      <div style={{ display: "flex", gap: 8 }}>
        <input
          placeholder="Add tag and press +"
          value={input}
          onChange={(e) => setInput(e.target.value)}
        />
        <button type="button" onClick={addTag}>
          +
        </button>
      </div>
    </div>
  );
}
