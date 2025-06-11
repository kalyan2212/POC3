import React, { useState } from 'react';
import axios from 'axios';
import './App.css';

function App() {
  const [selectedFile, setSelectedFile] = useState(null);
  const [uploadStatus, setUploadStatus] = useState('');
  const [diagramId, setDiagramId] = useState('');
  const [parsedData, setParsedData] = useState(null);

  const handleFileSelect = (event) => {
    setSelectedFile(event.target.files[0]);
    setUploadStatus('');
  };

  const handleUpload = async () => {
    if (!selectedFile) {
      setUploadStatus('Please select a file first');
      return;
    }

    const formData = new FormData();
    formData.append('file', selectedFile);
    formData.append('userId', 'demo-user');

    try {
      setUploadStatus('Uploading...');
      const response = await axios.post('/api/diagrams/upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });
      
      setDiagramId(response.data.diagramId);
      setUploadStatus(`Upload successful! Diagram ID: ${response.data.diagramId}`);
    } catch (error) {
      setUploadStatus('Upload failed: ' + error.message);
    }
  };

  const handleParse = async () => {
    if (!diagramId) {
      setUploadStatus('Please upload a file first');
      return;
    }

    try {
      setUploadStatus('Parsing...');
      const response = await axios.post(`/api/diagrams/${diagramId}/parse`);
      setParsedData(response.data);
      setUploadStatus('Parsing successful!');
    } catch (error) {
      setUploadStatus('Parsing failed: ' + error.message);
    }
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>EdgeConvert Microservices</h1>
        <p>Upload and parse Edge Diagrammer files</p>
        
        <div className="upload-section">
          <input 
            type="file" 
            accept=".edg"
            onChange={handleFileSelect}
          />
          <button onClick={handleUpload} disabled={!selectedFile}>
            Upload Diagram
          </button>
        </div>

        {diagramId && (
          <div className="parse-section">
            <button onClick={handleParse}>
              Parse Diagram
            </button>
          </div>
        )}

        {uploadStatus && (
          <div className="status">
            <p>{uploadStatus}</p>
          </div>
        )}

        {parsedData && (
          <div className="parsed-data">
            <h3>Parsed Data:</h3>
            <div className="tables">
              <h4>Tables ({parsedData.tables?.length || 0}):</h4>
              {parsedData.tables?.map((table, index) => (
                <div key={index} className="table-item">
                  <strong>{table.name}</strong> (ID: {table.numFigure})
                </div>
              ))}
            </div>
            <div className="fields">
              <h4>Fields ({parsedData.fields?.length || 0}):</h4>
              {parsedData.fields?.map((field, index) => (
                <div key={index} className="field-item">
                  <strong>{field.name}</strong> 
                  {field.isPrimaryKey && <span className="primary-key"> (PK)</span>}
                  (ID: {field.numFigure})
                </div>
              ))}
            </div>
          </div>
        )}
      </header>
    </div>
  );
}

export default App;