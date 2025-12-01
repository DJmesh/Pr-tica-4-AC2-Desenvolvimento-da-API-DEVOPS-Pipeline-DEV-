#!/bin/bash
cd "$(dirname "$0")"
echo "Abrindo relatórios..."
xdg-open "target/site/jacoco/index.html" 2>/dev/null &
sleep 2
xdg-open "target/pmd/pmd.html" 2>/dev/null &
sleep 2
xdg-open "target/surefire-reports/" 2>/dev/null &
echo "✅ Relatórios abertos!"
