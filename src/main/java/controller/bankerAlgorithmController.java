package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Arrays;

public class bankerAlgorithmController {
    @FXML private TextField txtProcesses;
    @FXML private TextField txtResources;
    @FXML private TextField txtProcessResources;
    @FXML private TextField txtAvailable;
    @FXML private TextArea txtAllocation;
    @FXML private TextArea txtMax;
    @FXML private TextArea txtResult;
    @FXML private TextArea txtNeed;
    @FXML private Button btnCheck;

    @FXML
    public void initialize() {
        btnCheck.setOnAction(event -> checkSafety());
    }

    private void checkSafety() {
        try {
            int numProcesses = Integer.parseInt(txtProcesses.getText().trim());
            int numResources = Integer.parseInt(txtResources.getText().trim());

            int[] processResources = parseArray(txtProcessResources.getText(), numResources);
            int[][] allocation = parseMatrix(txtAllocation.getText(), numProcesses, numResources);
            int[][] max = parseMatrix(txtMax.getText(), numProcesses, numResources);

            int[] available = calAvailable(numResources, numProcesses, allocation, max, processResources);
            int[][] need = calNeed(numProcesses, numResources, allocation, max);

            /**
             * check available
             */
            for (int i = 0; i < numResources; i++) {
                if (available[i] < 0) {
                    txtResult.setText("Error: Available resources cannot be negative!");
                    return;
                }
            }

            int[] work = Arrays.copyOf(available, numResources);
            boolean[] finish = new boolean[numProcesses];
            int[] safeSequence = new int[numProcesses];
            int count = 0;

            while (count < numProcesses) {
                boolean flag = false;
                for (int i = 0; i < numProcesses; i++) {
                    if (!finish[i] && canAllocate(i, max, work, allocation)) {
                        finish[i] = true;
                        safeSequence[count++] = i;
                        flag = true;
                        for (int j = 0; j < numResources; j++) {
                            work[j] += allocation[i][j];
                        }
                    }
                }
                if (!flag) {
                    txtNeed.setText(matrixToString(need));
                    txtAvailable.setText(Arrays.toString(available));
                    txtResult.setText("System is in an unsafe state");
                    return;
                }
            }
            txtNeed.setText(matrixToString(need));
            txtAvailable.setText(Arrays.toString(available));
            txtResult.setText("System is in a safe state\n Safe sequence: " + Arrays.toString(safeSequence));

        } catch (Exception e) {
            txtResult.setText("Invalid input! Please check your matrices.");
        }
    }

    public int[][] calNeed(int numProcesses, int numResources, int[][] allocation, int[][] max) {
        int[][] need = new int[numProcesses][numResources];
        for (int i = 0; i < numProcesses; i++) {
            for (int j = 0; j < numResources; j++) {
                need[i][j] = max[i][j] - allocation[i][j];
            }
        }
        return need;
    }

    public int[] calAvailable(int numResources, int numProcesses, int[][] allocation, int[][] max, int[] processResources) {
        int[] available = Arrays.copyOf(processResources, numResources);

        for (int i = 0; i < numProcesses; i++) {
            for (int j = 0; j < numResources; j++) {
                available[j] -= allocation[i][j];
            }
        }

        for (int i = 0; i < numResources; i++) {
            if (available[i] < 0) {
                throw new IllegalArgumentException("Error: Available resources cannot be negative.");
            }
        }

        return available;
    }

    public String matrixToString(int[][] matrix) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                sb.append(matrix[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public boolean canAllocate(int process, int[][] max, int[] work, int[][] allocation) {
        for (int i = 0; i < work.length; i++) {
            if (max[process][i] - allocation[process][i] > work[i]) {
                return false;
            }
        }
        return true;
    }

    public int[][] parseMatrix(String matrix, int numProcesses, int numResources) {
        int[][] matrixMatrix = new int[numProcesses][numResources];
        String[] lines = matrix.trim().split("\n");

        if (lines.length < numProcesses) {
            throw new IllegalArgumentException("Not enough rows in the matrix input.");
        }

        for (int i = 0; i < numProcesses; i++) {
            String[] values = lines[i].trim().split("\\s+");
            if (values.length < numResources) {
                throw new IllegalArgumentException("Not enough columns in row " + (i + 1));
            }
            for (int j = 0; j < numResources; j++) {
                try {
                    matrixMatrix[i][j] = Integer.parseInt(values[j]);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid number at row " + (i + 1) + ", column " + (j + 1));
                }
            }
        }
        return matrixMatrix;
    }


    public int[] parseArray(String array, int length) {
        String[] values = array.trim().split("\\s+");

        if (values.length < length) {
            throw new IllegalArgumentException("Not enough values in the array input.");
        }

        int[] arrayArray = new int[length];
        for (int i = 0; i < length; i++) {
            try {
                arrayArray[i] = Integer.parseInt(values[i]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid number at position " + (i + 1));
            }
        }
        return arrayArray;
    }

}
